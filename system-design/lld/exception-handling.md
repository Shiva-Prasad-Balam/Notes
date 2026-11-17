### Exception Handling
- Good exception handling makes your code robust without cluttering it. In machine coding rounds, custom exceptions signal domain-aware design. Throwing RuntimeException("something went wrong") everywhere signals the opposite.

### The Exception Hierarchy Strategy
- Two categories:
  - Business exceptions: Invalid domain operations (insufficient funds, slot taken, invalid state transition). The caller can potentially recover.
  - System exceptions: Infrastructure failures (DB down, timeout, disk full). Usually unrecoverable at the application level.

### Custom Exceptions Done Right

```java
// Base exception for the domain
public abstract class WalletException extends RuntimeException {
    private final String errorCode;

    protected WalletException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}

// Specific: carries context about what went wrong
public class InsufficientBalanceException extends WalletException {
    private final Money available;
    private final Money required;

    public InsufficientBalanceException(Money available, Money required) {
        super(String.format("Insufficient balance: have %s, need %s", available, required),
              "WALLET_001");
        this.available = available;
        this.required = required;
    }

    public Money getAvailable() { return available; }
    public Money getRequired() { return required; }
    public Money getDeficit() { return required.subtract(available); }
}

public class DuplicateTransactionException extends WalletException {
    private final String transactionId;

    public DuplicateTransactionException(String transactionId) {
        super("Transaction already processed: " + transactionId, "WALLET_002");
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
}

public class WalletFrozenException extends WalletException {
    private final String reason;
    private final Instant frozenSince;

    public WalletFrozenException(String walletId, String reason, Instant frozenSince) {
        super("Wallet " + walletId + " is frozen: " + reason, "WALLET_003");
        this.reason = reason;
        this.frozenSince = frozenSince;
    }
}

// Usage: meaningful, catchable, debuggable
public class WalletService {
    public void debit(String walletId, Money amount, String idempotencyKey) {
        Wallet wallet = repository.findById(walletId)
            .orElseThrow(() -> new WalletNotFoundException(walletId));

        if (wallet.isFrozen()) {
            throw new WalletFrozenException(walletId, wallet.getFreezeReason(), wallet.getFrozenAt());
        }

        if (transactionLog.exists(idempotencyKey)) {
            throw new DuplicateTransactionException(idempotencyKey);
        }

        if (wallet.getBalance().isLessThan(amount)) {
            throw new InsufficientBalanceException(wallet.getBalance(), amount);
        }

        wallet.debit(amount);
        transactionLog.record(idempotencyKey, amount);
    }
}
```
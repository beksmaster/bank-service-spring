CREATE TABLE accounts (
    account_number VARCHAR(32) PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,

    from_account VARCHAR(32) NOT NULL,
    to_account VARCHAR(32) NOT NULL,

    amount NUMERIC(19,2) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_transaction_from_account
                          FOREIGN KEY (from_account)
                          REFERENCES accounts(account_number),
    CONSTRAINT fk_transaction_to_account
                          FOREIGN KEY (to_account)
                          REFERENCES accounts(account_number)
)
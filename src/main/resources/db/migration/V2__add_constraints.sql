ALTER TABLE accounts
ADD CONSTRAINT chk_balance
CHECK (balance >= 0)
ALTER TABLE accounts
ADD COLUMN owner_id BIGINT;

ALTER TABLE accounts
ADD CONSTRAINT fk_account_owner
FOREIGN KEY (owner_id)
REFERENCES users(id);
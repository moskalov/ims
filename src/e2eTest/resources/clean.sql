SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE products RESTART IDENTITY;
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE roles RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

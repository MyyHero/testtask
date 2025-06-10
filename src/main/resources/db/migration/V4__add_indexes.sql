
CREATE INDEX IF NOT EXISTS idx_users_name_lower
    ON users ( lower(name) );

CREATE INDEX IF NOT EXISTS idx_users_dob
    ON users ( date_of_birth );

CREATE INDEX IF NOT EXISTS idx_email_user_id
    ON email_data ( user_id );

CREATE INDEX IF NOT EXISTS idx_phone_user_id
    ON phone_data ( user_id );

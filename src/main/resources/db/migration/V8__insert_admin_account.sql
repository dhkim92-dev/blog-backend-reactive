INSERT INTO member(
    id, nickname, email, password, role, is_blocked, created_at, updated_at
)
VALUES (
    '01994869-c3d0-787e-8a3b-184296276042',
    'admin',
    'admin@dohoon-kim.kr',
    '$2a$10$koUKrfV8WhUeV.a6.9FUzuOrOtFY.WUTF1foCRVtdVHUwQezPWbaC',
    'ADMIN',
    false,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
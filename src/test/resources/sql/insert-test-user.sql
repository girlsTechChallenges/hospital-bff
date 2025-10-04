-- Insert test type first
INSERT INTO tipos (id, nome_tipo) VALUES (1, 'CLIENTE');

-- Insert test user for integration tests  
INSERT INTO usuarios (id, nome, email, login, senha, data_alteracao, tipo_id) 
VALUES (1, 'João Silva', 'joao.silva@test.com', 'joaosilva', '$2a$12$encrypted_password', '2024-01-01', 1);


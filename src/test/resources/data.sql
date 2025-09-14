-- Inserção de dados na tabela de tipos
INSERT INTO tipos (id, tipo)
VALUES
    (1, 'NURSE',''),
    (2, 'DOCTOR'),
    (2, 'PATIENT','');

-- Inserção de dados na tabela de usuários
INSERT INTO usuarios (id, nome, email, login, data_alteracao, tipo, senha)
VALUES
    (1, 'João Silva', 'joao.silva@example.com', 'joaosilva', '2025-05-10', 'CLIENTE', 'senha123'),
    (2, 'Maria Oliveira', 'maria.oliveira@example.com', 'mariaoliveira', '2025-05-10', 'DONO', 'senha456'),
    (3, 'Carlos Souza', 'carlos.souza@example.com', 'carlossouza', '2025-05-10', 'CLIENTE', 'senha789');


--Insercao de dados na tabela roles
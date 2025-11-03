-- Inserir usuários
INSERT IGNORE INTO usuarios (nome, email, senha, matricula, data_nascimento)
VALUES
('Administrador', 'admin@empresa.com', 'admin123', '00001', '1990-01-01'),
('Maria Silva', 'maria.silva@empresa.com', '123456', '00002', '1992-04-15'),
('João Pereira', 'joao.pereira@empresa.com', '654321', '00003', '1988-11-30');

-- Inserir recursos
INSERT IGNORE INTO recursos (descricao, tipo, dias_semana_disponivel, data_inicial_agendamento, data_final_agendamento, hora_inicial_agendamento, hora_final_agendamento)
VALUES
('Sala de Reunião 1', 'Espaço', 'segunda-feira,terça-feira,quarta-feira,quinta-feira,sexta-feira', CURDATE(), '2025-12-31', '08:00:00', '18:00:00'),
('Projetor Multimídia', 'Equipamento', 'segunda-feira,terça-feira,quarta-feira,quinta-feira,sexta-feira', CURDATE(), '2025-12-31', '08:00:00', '18:00:00'),
('Auditório Principal', 'Espaço', 'segunda-feira,quarta-feira,sexta-feira', CURDATE(), '2025-12-31', '09:00:00', '17:00:00');

-- Inserir reservas
INSERT IGNORE INTO reservas (colaborador_id, recurso_id, data, hora_inicial, hora_final, data_cancelamento, observacao)
VALUES
(1, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00', '10:00:00', NULL, 'Reunião de planejamento com equipe'),
(2, 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '10:30:00', '11:30:00', NULL, 'Apresentação do novo projeto'),
(3, 3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '14:00:00', '15:00:00', CURDATE(), 'Reserva cancelada por conflito de agenda');

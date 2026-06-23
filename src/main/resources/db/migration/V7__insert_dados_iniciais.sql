-- 1. Inserindo o usuário ADMIN
-- A senha '123456' em hash BCrypt é: $2a$10$EblZqNptyYvcLm/VwDCVAuAw5QkEX/WjD.Z8Y.IopvJ3F9nEw3Y2S
INSERT INTO tb_usuario (nome, email, senha, role)
VALUES ('Administrador', 'admin@admin.com', '$2a$10$7L8XVlffpfBf1kAwSs2zmu7jt.J1MpXiON/dDeyVP3JWcgOGrUS8u', 'ROLE_ADMIN');*/

-- 2. Inserindo uma Unidade padrão (Você precisa de uma unidade para vincular os produtos depois)
INSERT INTO tb_unidade (nome, cidade, uf, cozinha_completa)
VALUES ('Matriz Hortolândia', 'Hortolândia', 'SP', true);

-- Inserindo pratos típicos do Nordeste
INSERT INTO tb_produto (nome, descricao) VALUES
                                              ('Baião de Dois Tradicional', 'Mistura clássica de arroz, feijão fradinho, queijo coalho em cubos, carne de sol desfiada, bacon e coentro fresco.'),

                                              ('Carne de Sol com Macaxeira', 'Saborosas tiras de carne de sol acebolada, acompanhadas de macaxeira (mandioca) frita bem crocante e manteiga de garrafa.'),

                                              ('Acarajé Completo', 'Bolinho de feijão fradinho frito no azeite de dendê, recheado com vatapá, caruru, camarão seco e vinagrete.'),

                                              ('Tapioca de Carne Seca', 'Goma de tapioca artesanal recheada com carne seca desfiada e generosos pedaços de queijo coalho derretido.'),

                                              ('Moqueca de Peixe com Pirão', 'Postas de peixe cozidas no leite de coco, azeite de dendê e pimentões. Acompanha arroz branco e pirão cremoso.'),

                                              ('Escondidinho de Jerimum', 'Purê cremoso de abóbora (jerimum) recheado com charque desfiada e gratinado com queijo de coalho.'),

                                              ('Cuscuz Nordestino com Charque', 'Cuscuz de milho no vapor, umedecido com manteiga da terra e acompanhado de charque acebolada.'),

                                              ('Cartola Pernambucana', 'Sobremesa típica feita com camadas de banana frita, queijo manteiga derretido, polvilhada com açúcar e canela.'),

                                              ('Bolo de Rolo', 'Fatias finas de pão de ló enroladas com um recheio farto de goiabada derretida. Um clássico de Pernambuco.'),

                                              ('Cajuína Gelada 330ml', 'Bebida típica nordestina, refrescante e sem álcool, feita a partir do suco de caju clarificado.');
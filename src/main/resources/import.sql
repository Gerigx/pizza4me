INSERT INTO Pizza (id, name, beschreibung, price, sellCounter) VALUES 
(1, 'Margherita', 'Klassische Pizza mit Tomatensauce, Mozzarella und frischem Basilikum', 8.50, 245),
(2, 'Salami', 'Herzhaft mit italienischer Salami, Tomatensauce und Mozzarella', 9.80, 198),
(3, 'Prosciutto', 'Mit luftgetrocknetem Parmaschinken, Rucola und Parmesanspänen', 12.90, 156),
(4, 'Funghi', 'Frische Champignons, Tomatensauce, Mozzarella und Oregano', 9.20, 134),
(5, 'Quattro Stagioni', 'Vier Jahreszeiten mit Schinken, Pilzen, Artischocken und Oliven', 13.50, 89),
(6, 'Diavola', 'Scharfe Salami, Tomatensauce, Mozzarella und Peperoni', 10.80, 167),
(7, 'Capricciosa', 'Schinken, Pilze, Artischocken, Oliven und Mozzarella', 12.20, 112),
(8, 'Quattro Formaggi', 'Vier-Käse-Pizza mit Mozzarella, Gorgonzola, Parmesan und Pecorino', 11.90, 78),
(9, 'Tonno', 'Thunfisch, rote Zwiebeln, Kapern und Mozzarella', 11.50, 95),
(10, 'Vegetariana', 'Paprika, Zucchini, Auberginen, Tomaten und Mozzarella', 10.90, 123),
(11, 'Calzone', 'Gefaltete Pizza mit Schinken, Pilzen und Mozzarella', 12.80, 67),
(12, 'Siciliana', 'Sardellen, Kapern, Oliven, Tomatensauce und Mozzarella', 11.80, 45),
(13, 'Marinara', 'Nur Tomatensauce, Knoblauch, Oregano und Olivenöl - vegan', 7.90, 89),
(14, 'Rucola', 'Parmaschinken, Rucola, Kirschtomaten und Parmesanspäne', 13.20, 145),
(15, 'Spinaci', 'Spinat, Ricotta, Knoblauch und Mozzarella', 10.50, 76),
(16, 'Salmone', 'Geräucherter Lachs, Crème fraîche, Dill und rote Zwiebeln', 14.90, 34),
(17, 'Bresaola', 'Luftgetrocknetes Rindfleisch, Rucola, Parmesan und Balsamico', 15.50, 28),
(18, 'Parmigiana', 'Auberginen, Tomatensauce, Parmesan und Basilikum', 11.20, 67),
(19, 'Mortadella', 'Mortadella, Pistazien, Stracciatella und Rucola', 13.80, 52),
(20, 'Nduja', 'Scharfe kalabrische Wurst, Mozzarella und Honig', 12.50, 43),
(21, 'Tartufo', 'Trüffelcreme, Pilze, Mozzarella und Parmesan', 16.90, 19);

ALTER SEQUENCE pizza_seq RESTART WITH 22;
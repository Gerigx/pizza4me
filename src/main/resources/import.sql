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

-- Kunden-Testdaten
-- Password für alle: "password123" (gehashed mit BCrypt)
INSERT INTO Kunde (id, vorname, nachname, username, password, role, strasse, hausnummer, plz, ort, land, orders) VALUES 
(1, 'Max', 'Mustermann', 'admin', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'ADMIN', 'Musterstraße', '1', '12345', 'Berlin', 'Deutschland', NULL),
(2, 'Anna', 'Schmidt', 'anna.schmidt', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Hauptstraße', '42', '80331', 'München', 'Deutschland', NULL),
(3, 'Peter', 'Müller', 'peter.mueller', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Kirchstraße', '15', '20095', 'Hamburg', 'Deutschland', NULL),
(4, 'Lisa', 'Wagner', 'lisa.wagner', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Rosenstraße', '7', '50667', 'Köln', 'Deutschland', NULL),
(5, 'Thomas', 'Becker', 'thomas.becker', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Bahnhofstraße', '23', '70173', 'Stuttgart', 'Deutschland', NULL),
(6, 'Sarah', 'Fischer', 'sarah.fischer', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Gartenstraße', '8', '60311', 'Frankfurt', 'Deutschland', NULL),
(7, 'Michael', 'Weber', 'michael.weber', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Schulstraße', '12', '90402', 'Nürnberg', 'Deutschland', NULL),
(8, 'Julia', 'Meyer', 'julia.meyer', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Marktplatz', '5', '01067', 'Dresden', 'Deutschland', NULL),
(9, 'David', 'Richter', 'david.richter', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Lindenstraße', '33', '30159', 'Hannover', 'Deutschland', NULL),
(10, 'Stefanie', 'Koch', 'stefanie.koch', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Am Stadtpark', '18', '28203', 'Bremen', 'Deutschland', NULL),
(11, 'Markus', 'Zimmermann', 'markus.zimmermann', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Kastanienallee', '44', '10435', 'Berlin', 'Deutschland', NULL),
(12, 'Melanie', 'Hoffmann', 'melanie.hoffmann', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Dorfstraße', '2', '49584', 'Fürstenau', 'Deutschland', NULL),
(13, 'Oliver', 'Neumann', 'oliver.neumann', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Eichenweg', '9', '49074', 'Osnabrück', 'Deutschland', NULL),
(14, 'Claudia', 'Braun', 'claudia.braun', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Bergstraße', '16', '76131', 'Karlsruhe', 'Deutschland', NULL),
(15, 'Andreas', 'Krause', 'andreas.krause', '$2a$10$089kRT7Qijy.lpBVpjjSVOlw/HG6ARopyf1dBsaFYanXzfMl0MJv2', 'USER', 'Sonnenstraße', '27', '45127', 'Essen', 'Deutschland', NULL);

-- Sequences anpassen
ALTER SEQUENCE pizza_seq RESTART WITH 22;
ALTER SEQUENCE kunde_seq RESTART WITH 16;
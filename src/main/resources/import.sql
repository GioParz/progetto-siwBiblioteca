-- ====================================================================
-- 1. PULIZIA DI SICUREZZA CON CASCADE (rende lo script ri-eseguibile)
-- ====================================================================
TRUNCATE TABLE elemento_libreria CASCADE;
TRUNCATE TABLE libro CASCADE;
TRUNCATE TABLE autore CASCADE;
TRUNCATE TABLE genere CASCADE;
TRUNCATE TABLE credentials CASCADE;
TRUNCATE TABLE users CASCADE;

-- Riporto le sequence a 1 per avere id puliti e prevedibili ad ogni riavvio
ALTER SEQUENCE autore_seq RESTART WITH 1;
ALTER SEQUENCE genere_seq RESTART WITH 1;
ALTER SEQUENCE libro_seq RESTART WITH 1;
ALTER SEQUENCE utente_seq RESTART WITH 1;
ALTER SEQUENCE credentials_seq RESTART WITH 1;
ALTER SEQUENCE elementolibreria_seq RESTART WITH 1;


-- ====================================================================
-- 2. GENERI LETTERARI
-- ====================================================================
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Narrativa Classica', 'Grandi romanzi che hanno attraversato i secoli.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Fantasy', 'Mondi immaginari, magia e avventure epiche.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Giallo', 'Indagini, misteri e colpi di scena.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Fantascienza', 'Futuro, tecnologia e viaggi tra le stelle.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Distopico', 'Società oppressive e futuri inquietanti.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Saggistica', 'Testi di approfondimento e non-fiction.');


-- ====================================================================
-- 3. AUTORI
-- ====================================================================
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Fëdor', 'Dostoevskij', 'Russa', 'Scrittore e filosofo russo, tra i massimi autori della letteratura mondiale.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Italo', 'Calvino', 'Italiana', 'Scrittore e giornalista, tra le voci più originali della letteratura italiana del Novecento.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'J.R.R.', 'Tolkien', 'Britannica', 'Filologo e scrittore, padre della moderna narrativa fantasy.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Ursula K.', 'Le Guin', 'Statunitense', 'Autrice di fantascienza e fantasy nota per la profondità dei suoi mondi immaginari.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Agatha', 'Christie', 'Britannica', 'La "Regina del Giallo", autrice dei celebri Hercule Poirot e Miss Marple.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Arthur Conan', 'Doyle', 'Britannica', 'Creatore del celebre investigatore Sherlock Holmes.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'George', 'Orwell', 'Britannica', 'Scrittore e saggista, noto per le sue opere di critica politica e sociale.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Isaac', 'Asimov', 'Statunitense', 'Biochimico e scrittore, uno dei padri della fantascienza moderna.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Yuval Noah', 'Harari', 'Israeliana', 'Storico e saggista, autore di opere divulgative di grande successo internazionale.');


-- ====================================================================
-- 4. LIBRI (ogni riga collega autore e genere tramite una piccola query)
-- ====================================================================
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Delitto e Castigo', '9788807900012', 1866, 'Raskol''nikov, giovane studente pietroburghese, uccide un''anziana usuraia convinto di essere un uomo superiore alla morale comune.', '', (SELECT id FROM autore WHERE cognome = 'Dostoevskij'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'I Fratelli Karamazov', '9788807900029', 1880, 'La storia di tre fratelli e del loro complesso rapporto con il padre, sullo sfondo di temi religiosi ed esistenziali.', '', (SELECT id FROM autore WHERE cognome = 'Dostoevskij'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Barone Rampante', '9788804668237', 1957, 'Cosimo Piovasco di Rondò decide di vivere sugli alberi, senza mai più toccare terra, osservando il mondo da una prospettiva unica.', '', (SELECT id FROM autore WHERE cognome = 'Calvino'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Lo Hobbit', '9788845292613', 1937, 'Bilbo Baggins viene coinvolto in un''avventura inaspettata alla ricerca di un tesoro custodito dal drago Smaug.', '', (SELECT id FROM autore WHERE cognome = 'Tolkien'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Signore degli Anelli', '9788845292620', 1954, 'La compagnia dell''anello intraprende un viaggio per distruggere l''Unico Anello e salvare la Terra di Mezzo da Sauron.', '', (SELECT id FROM autore WHERE cognome = 'Tolkien'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Mago di Terramare', '9788834728430', 1968, 'Ged, giovane mago dal grande potenziale, dovrà affrontare un''ombra oscura da lui stesso liberata.', '', (SELECT id FROM autore WHERE cognome = 'Le Guin'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Assassinio sull''Orient Express', '9788804668817', 1934, 'Hercule Poirot deve risolvere un omicidio avvenuto su un treno bloccato dalla neve, con dodici sospettati a bordo.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Dieci Piccoli Indiani', '9788804668824', 1939, 'Dieci sconosciuti vengono attirati su un''isola deserta e uccisi uno a uno, secondo una filastrocca.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Uno Studio in Rosso', '9788807888920', 1887, 'Il primo caso di Sherlock Holmes e del dottor Watson, alle prese con un misterioso omicidio a Londra.', '', (SELECT id FROM autore WHERE cognome = 'Doyle'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), '1984', '9788804668848', 1949, 'Winston Smith vive in un regime totalitario che controlla ogni aspetto della vita attraverso il Grande Fratello.', '', (SELECT id FROM autore WHERE cognome = 'Orwell'), (SELECT id FROM genere WHERE nome = 'Distopico'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'La Fattoria degli Animali', '9788804668855', 1945, 'Gli animali di una fattoria si ribellano al padrone umano, per poi ricadere in una nuova forma di tirannia.', '', (SELECT id FROM autore WHERE cognome = 'Orwell'), (SELECT id FROM genere WHERE nome = 'Distopico'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Fondazione', '9788804668862', 1951, 'Hari Seldon prevede la caduta dell''Impero Galattico e fonda una società per preservare la conoscenza umana.', '', (SELECT id FROM autore WHERE cognome = 'Asimov'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Io, Robot', '9788804668879', 1950, 'Una raccolta di racconti che esplora il rapporto tra uomo e robot attraverso le celebri Tre Leggi della Robotica.', '', (SELECT id FROM autore WHERE cognome = 'Asimov'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Sapiens. Da Animali a Dei', '9788858030348', 2011, 'Un viaggio nella storia dell''umanità, dalla comparsa dell''Homo Sapiens fino alle sfide del presente.', '', (SELECT id FROM autore WHERE cognome = 'Harari'), (SELECT id FROM genere WHERE nome = 'Saggistica'));


-- ====================================================================
-- 5. UTENTI E CREDENZIALI
-- (Le password sono già cifrate con BCrypt: vedi il commento per la password in chiaro)
-- ====================================================================

-- Amministratore (username: admin | password: admin123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Direttrice', 'Biblioteca', 'direzione@bibliohub.it');
INSERT INTO credentials (id, username, password, ruolo, utente_id) VALUES (nextval('credentials_seq'), 'admin', '$2a$10$1pWD29vOPWS1oSjaSR41wO9kYEdt0.jSczqUQoFRRdnDMKHT0iQtm', 'ADMIN', (SELECT id FROM users WHERE cognome = 'Biblioteca'));

-- Utente registrata "grande lettrice" (username: lucia | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Lucia', 'Bianchi', 'lucia.bianchi@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id) VALUES (nextval('credentials_seq'), 'lucia', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Bianchi'));

-- Utente registrato "lettore occasionale" (username: marco | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Marco', 'Verdi', 'marco.verdi@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id) VALUES (nextval('credentials_seq'), 'marco', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Verdi'));


-- ====================================================================
-- 6. LIBRERIA PERSONALE (ElementoLibreria)
-- ====================================================================

-- --- Libreria di Lucia: grande appassionata di Fantasy, ha votato bene più libri
--     dello stesso genere -> dovrebbe attivare bene il motore di consigli (CU-6)
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Lo Hobbit'), 'COMPLETATO', 5, '2026-05-02');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Il Signore degli Anelli'), 'COMPLETATO', 5, '2026-05-20');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Delitto e Castigo'), 'IN_LETTURA', NULL, '2026-06-10');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = '1984'),'DA_LEGGERE', NULL, '2026-07-01');

-- --- Libreria di Marco: preferisce il Giallo, con un libro anche non ancora valutato
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Assassinio sull''Orient Express'), 'COMPLETATO', 5, '2026-04-15');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Dieci Piccoli Indiani'), 'COMPLETATO', 4, '2026-05-05');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Fondazione'), 'DA_LEGGERE', NULL, '2026-07-10');

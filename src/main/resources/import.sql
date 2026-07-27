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
-- 2. GENERI LETTERARI (invariati rispetto alla tua versione originale)
-- ====================================================================
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Narrativa Classica', 'Grandi romanzi che hanno attraversato i secoli.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Fantasy', 'Mondi immaginari, magia e avventure epiche.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Giallo', 'Indagini, misteri e colpi di scena.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Fantascienza', 'Futuro, tecnologia e viaggi tra le stelle.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Distopico', 'Società oppressive e futuri inquietanti.');
INSERT INTO genere (id, nome, descrizione) VALUES (nextval('genere_seq'), 'Saggistica', 'Testi di approfondimento e non-fiction.');


-- ====================================================================
-- 3. AUTORI (i 9 originali + 6 nuovi, per dare più profondità a ogni genere)
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
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Gabriel', 'García Márquez', 'Colombiana', 'Maestro del realismo magico, premio Nobel per la Letteratura nel 1982.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Patricia', 'Highsmith', 'Statunitense', 'Autrice di thriller psicologici e romanzi gialli dallo stile inconfondibile.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Robin', 'Hobb', 'Statunitense', 'Autrice fantasy nota per la saga dell''Assassino di Re.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Philip K.', 'Dick', 'Statunitense', 'Autore di fantascienza visionaria, esplora identità e percezione della realtà.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Margaret', 'Atwood', 'Canadese', 'Autrice canadese nota per i suoi romanzi distopici e femministi.');
INSERT INTO autore (id, nome, cognome, nazionalita, biografia) VALUES (nextval('autore_seq'), 'Daniel', 'Kahneman', 'Israelo-statunitense', 'Psicologo e premio Nobel per l''Economia, pioniere dell''economia comportamentale.');


-- ====================================================================
-- 4. LIBRI (38 titoli: i 15 originali + 23 nuovi, per dare a ogni autore/genere
--    abbastanza titoli "di riserva" da poter essere effettivamente consigliati)
-- ====================================================================

-- --- Dostoevskij (Narrativa Classica) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Delitto e Castigo', '9788807900012', 1866, 'Raskol''nikov, giovane studente pietroburghese, uccide un''anziana usuraia convinto di essere un uomo superiore alla morale comune.', '', (SELECT id FROM autore WHERE cognome = 'Dostoevskij'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'I Fratelli Karamazov', '9788807900029', 1880, 'La storia di tre fratelli e del loro complesso rapporto con il padre, sullo sfondo di temi religiosi ed esistenziali.', '', (SELECT id FROM autore WHERE cognome = 'Dostoevskij'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'L''Idiota', '9788807900036', 1869, 'Il principe Myškin, uomo di straordinaria bontà e ingenuità, si scontra con l''ipocrisia della società russa ottocentesca.', '', (SELECT id FROM autore WHERE cognome = 'Dostoevskij'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));

-- --- Calvino (Narrativa Classica) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Barone Rampante', '9788804668237', 1957, 'Cosimo Piovasco di Rondò decide di vivere sugli alberi, senza mai più toccare terra, osservando il mondo da una prospettiva unica.', '', (SELECT id FROM autore WHERE cognome = 'Calvino'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Visconte Dimezzato', '9788804668244', 1952, 'Il visconte Medardo di Terralba viene diviso in due metà, una buona e una cattiva, da una cannonata in battaglia.', '', (SELECT id FROM autore WHERE cognome = 'Calvino'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Le Città Invisibili', '9788804668251', 1972, 'Marco Polo racconta a Kublai Khan le meravigliose e impossibili città del suo impero, sospese tra sogno e realtà.', '', (SELECT id FROM autore WHERE cognome = 'Calvino'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));

-- --- García Márquez (Narrativa Classica) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Cent''anni di Solitudine', '9788804668268', 1967, 'La saga della famiglia Buendía nell''arco di sette generazioni, nel villaggio immaginario di Macondo.', '', (SELECT id FROM autore WHERE cognome = 'García Márquez'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'L''Amore ai Tempi del Colera', '9788804668275', 1985, 'Una storia d''amore che attraversa più di cinquant''anni, tra passione giovanile e fedeltà adulta.', '', (SELECT id FROM autore WHERE cognome = 'García Márquez'), (SELECT id FROM genere WHERE nome = 'Narrativa Classica'));

-- --- Tolkien (Fantasy) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Lo Hobbit', '9788845292613', 1937, 'Bilbo Baggins viene coinvolto in un''avventura inaspettata alla ricerca di un tesoro custodito dal drago Smaug.', '', (SELECT id FROM autore WHERE cognome = 'Tolkien'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Signore degli Anelli', '9788845292620', 1954, 'La compagnia dell''anello intraprende un viaggio per distruggere l''Unico Anello e salvare la Terra di Mezzo da Sauron.', '', (SELECT id FROM autore WHERE cognome = 'Tolkien'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Silmarillion', '9788845292637', 1977, 'La storia della creazione della Terra di Mezzo e degli eventi che precedono Lo Hobbit e Il Signore degli Anelli.', '', (SELECT id FROM autore WHERE cognome = 'Tolkien'), (SELECT id FROM genere WHERE nome = 'Fantasy'));

-- --- Le Guin (Fantasy + Fantascienza: un''autrice presente in due generi) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Mago di Terramare', '9788834728430', 1968, 'Ged, giovane mago dal grande potenziale, dovrà affrontare un''ombra oscura da lui stesso liberata.', '', (SELECT id FROM autore WHERE cognome = 'Le Guin'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Le Tombe di Atuan', '9788834728447', 1970, 'Tenar, giovane sacerdotessa, incontra Ged nei tunnel sotterranei delle Tombe di Atuan.', '', (SELECT id FROM autore WHERE cognome = 'Le Guin'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'I Reietti dell''Altro Pianeta', '9788834728454', 1974, 'Il fisico Shevek viaggia tra due mondi gemelli con sistemi sociali opposti, in cerca di una sintesi tra libertà e comunità.', '', (SELECT id FROM autore WHERE cognome = 'Le Guin'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));

-- --- Robin Hobb (Fantasy) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'L''Assassino di Corte', '9788834728461', 1995, 'FitzChevalier, figlio illegittimo di un principe, viene addestrato come assassino al servizio della corona.', '', (SELECT id FROM autore WHERE cognome = 'Hobb'), (SELECT id FROM genere WHERE nome = 'Fantasy'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'La Nave della Magia', '9788834728478', 1998, 'Le vicende della famiglia Vestrit e della loro nave vivente, in un mondo di traffici, pirati e creature magiche.', '', (SELECT id FROM autore WHERE cognome = 'Hobb'), (SELECT id FROM genere WHERE nome = 'Fantasy'));

-- --- Christie (Giallo) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Assassinio sull''Orient Express', '9788804668817', 1934, 'Hercule Poirot deve risolvere un omicidio avvenuto su un treno bloccato dalla neve, con dodici sospettati a bordo.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Dieci Piccoli Indiani', '9788804668824', 1939, 'Dieci sconosciuti vengono attirati su un''isola deserta e uccisi uno a uno, secondo una filastrocca.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Poirot a Styles Court', '9788804668831', 1920, 'Il primo caso di Hercule Poirot: un omicidio in una tenuta di campagna inglese durante la Prima Guerra Mondiale.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Nemesi', '9788804668941', 1971, 'Miss Marple indaga su un vecchio delitto rimasto irrisolto, seguendo un misterioso itinerario turistico organizzato.', '', (SELECT id FROM autore WHERE cognome = 'Christie'), (SELECT id FROM genere WHERE nome = 'Giallo'));

-- --- Doyle (Giallo) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Uno Studio in Rosso', '9788807888920', 1887, 'Il primo caso di Sherlock Holmes e del dottor Watson, alle prese con un misterioso omicidio a Londra.', '', (SELECT id FROM autore WHERE cognome = 'Doyle'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Mastino dei Baskerville', '9788807888937', 1902, 'Holmes e Watson indagano su una leggendaria bestia infernale che perseguita la famiglia Baskerville nella brughiera del Devon.', '', (SELECT id FROM autore WHERE cognome = 'Doyle'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Segno dei Quattro', '9788807888944', 1890, 'Un tesoro scomparso, una promessa mantenuta dopo anni e un delitto: il secondo caso di Sherlock Holmes.', '', (SELECT id FROM autore WHERE cognome = 'Doyle'), (SELECT id FROM genere WHERE nome = 'Giallo'));

-- --- Highsmith (Giallo) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Talento di Mr. Ripley', '9788807888951', 1955, 'Tom Ripley, affascinante truffatore, si insinua nella vita di un ricco americano a Venezia fino a sostituirsi a lui.', '', (SELECT id FROM autore WHERE cognome = 'Highsmith'), (SELECT id FROM genere WHERE nome = 'Giallo'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Sconosciuti in Treno', '9788807888968', 1950, 'Un incontro casuale in treno si trasforma in un patto scellerato: scambiarsi gli omicidi per non essere scoperti.', '', (SELECT id FROM autore WHERE cognome = 'Highsmith'), (SELECT id FROM genere WHERE nome = 'Giallo'));

-- --- Orwell (Distopico + Saggistica: un altro autore presente in due generi) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), '1984', '9788804668848', 1949, 'Winston Smith vive in un regime totalitario che controlla ogni aspetto della vita attraverso il Grande Fratello.', '', (SELECT id FROM autore WHERE cognome = 'Orwell'), (SELECT id FROM genere WHERE nome = 'Distopico'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'La Fattoria degli Animali', '9788804668855', 1945, 'Gli animali di una fattoria si ribellano al padrone umano, per poi ricadere in una nuova forma di tirannia.', '', (SELECT id FROM autore WHERE cognome = 'Orwell'), (SELECT id FROM genere WHERE nome = 'Distopico'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Omaggio alla Catalogna', '9788804668862', 1938, 'Il resoconto autobiografico dell''esperienza di Orwell come miliziano durante la Guerra Civile Spagnola.', '', (SELECT id FROM autore WHERE cognome = 'Orwell'), (SELECT id FROM genere WHERE nome = 'Saggistica'));

-- --- Atwood (Distopico) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Racconto dell''Ancella', '9788804668879', 1985, 'In una teocrazia totalitaria, le donne fertili sono ridotte a strumenti di riproduzione al servizio del regime.', '', (SELECT id FROM autore WHERE cognome = 'Atwood'), (SELECT id FROM genere WHERE nome = 'Distopico'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'L''Altro Inizio', '9788804668886', 2003, 'Dopo una catastrofe globale, Jimmy si ritrova forse ultimo uomo sulla Terra, tra i ricordi del mondo che fu e nuove creature.', '', (SELECT id FROM autore WHERE cognome = 'Atwood'), (SELECT id FROM genere WHERE nome = 'Distopico'));

-- --- Asimov (Fantascienza) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Fondazione', '9788804668893', 1951, 'Hari Seldon prevede la caduta dell''Impero Galattico e fonda una società per preservare la conoscenza umana.', '', (SELECT id FROM autore WHERE cognome = 'Asimov'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Io, Robot', '9788804668909', 1950, 'Una raccolta di racconti che esplora il rapporto tra uomo e robot attraverso le celebri Tre Leggi della Robotica.', '', (SELECT id FROM autore WHERE cognome = 'Asimov'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Fondazione e Impero', '9788804668916', 1952, 'Il secondo capitolo del ciclo della Fondazione: il Mulo, un mutante imprevisto, minaccia il piano di Hari Seldon.', '', (SELECT id FROM autore WHERE cognome = 'Asimov'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));

-- --- Philip K. Dick (Fantascienza) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Il Cacciatore di Androidi', '9788804668923', 1968, 'In una Terra post-apocalittica, il cacciatore di taglie Rick Deckard dà la caccia ad androidi che si spacciano per umani.', '', (SELECT id FROM autore WHERE cognome = 'Dick'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Ubik', '9788804668930', 1969, 'Un gruppo di persone si ritrova intrappolato in una realtà che si dissolve progressivamente, tra vita e morte.', '', (SELECT id FROM autore WHERE cognome = 'Dick'), (SELECT id FROM genere WHERE nome = 'Fantascienza'));

-- --- Harari (Saggistica) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Sapiens. Da Animali a Dei', '9788858030348', 2011, 'Un viaggio nella storia dell''umanità, dalla comparsa dell''Homo Sapiens fino alle sfide del presente.', '', (SELECT id FROM autore WHERE cognome = 'Harari'), (SELECT id FROM genere WHERE nome = 'Saggistica'));
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Homo Deus', '9788858030355', 2015, 'Uno sguardo sulle possibili sfide future dell''umanità: intelligenza artificiale, biotecnologie e ricerca dell''immortalità.', '', (SELECT id FROM autore WHERE cognome = 'Harari'), (SELECT id FROM genere WHERE nome = 'Saggistica'));

-- --- Kahneman (Saggistica) ---
INSERT INTO libro (id, titolo, isbn, anno_pubblicazione, trama, copertina_url, autore_id, genere_id) VALUES (nextval('libro_seq'), 'Pensieri Lenti e Veloci', '9788858030362', 2011, 'Un''analisi dei due sistemi di pensiero che guidano le nostre decisioni: uno intuitivo e veloce, l''altro razionale e lento.', '', (SELECT id FROM autore WHERE cognome = 'Kahneman'), (SELECT id FROM genere WHERE nome = 'Saggistica'));


-- ====================================================================
-- 5. UTENTI E CREDENZIALI
-- (Le password sono già cifrate con BCrypt: vedi il commento per la password in chiaro.
--  Per semplicità tutti gli utenti di test condividono la stessa password "user123".)
-- ====================================================================

-- Amministratore (username: admin | password: admin123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Direttrice', 'Biblioteca', 'direzione@bibliohub.it');
INSERT INTO credentials (id, username, password, ruolo, utente_id, oauth) VALUES (nextval('credentials_seq'), 'admin', '$2a$10$1pWD29vOPWS1oSjaSR41wO9kYEdt0.jSczqUQoFRRdnDMKHT0iQtm', 'ADMIN', (SELECT id FROM users WHERE cognome = 'Biblioteca'), false);

-- Lucia: grande appassionata di Fantasy, in particolare di Tolkien (username: lucia | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Lucia', 'Bianchi', 'lucia.bianchi@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id, oauth) VALUES (nextval('credentials_seq'), 'lucia', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Bianchi'), false);

-- Marco: appassionato di Giallo, in particolare di Christie (username: marco | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Marco', 'Verdi', 'marco.verdi@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id, oauth) VALUES (nextval('credentials_seq'), 'marco', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Verdi'), false);

-- Giulia: "completista" di Orwell in due generi diversi (Distopico + Saggistica),
-- utile per verificare che il suggerimento per autore preferito funzioni anche a cavallo
-- tra generi diversi (username: giulia | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Giulia', 'Rossi', 'giulia.rossi@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id, oauth) VALUES (nextval('credentials_seq'), 'giulia', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Rossi'), false);

-- Paolo: utente appena iscritto, con un solo libro da leggere e nessuna
-- valutazione. Serve a verificare il "caso limite": il motore dei consigli deve restare
-- vuoto (nessun fallback generico) finché non ci sono abbastanza dati (username: paolo | password: user123)
INSERT INTO users (id, nome, cognome, email) VALUES (nextval('utente_seq'), 'Paolo', 'Ferrari', 'paolo.ferrari@gmail.com');
INSERT INTO credentials (id, username, password, ruolo, utente_id, oauth) VALUES (nextval('credentials_seq'), 'paolo', '$2a$10$xq1wMvOsPJekW7FF..Xc4.cynxv59yB9gPtrwPeY/.RC5meVVxqRC', 'USER', (SELECT id FROM users WHERE cognome = 'Ferrari'), false);


-- ====================================================================
-- 6. LIBRERIA PERSONALE (ElementoLibreria)
-- Ogni libreria è costruita apposta per testare uno scenario preciso del motore dei
-- consigli (vedi i commenti). In tutti i casi viene lasciato deliberatamente ALMENO un
-- libro "non letto" per ogni autore/genere preferito, altrimenti non ci sarebbe nulla
-- da consigliare.
-- ====================================================================

-- --- Lucia: 3 libri Fantasy valutati 4-5 stelle (2 di Tolkien, 1 di Le Guin) -> ci si
--     aspetta che "Il Silmarillion" (Tolkien, non ancora letto) sia il primo consiglio per
--     autore preferito, e libri Fantasy di altri autori (Le Guin, Hobb) come consiglio per
--     genere preferito. Include anche 2 libri non valutati, per un quadro realistico.
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Lo Hobbit'), 'COMPLETATO', 5, '2026-05-02');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Il Signore degli Anelli'), 'COMPLETATO', 5, '2026-05-20');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Il Mago di Terramare'), 'COMPLETATO', 4, '2026-06-01');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = 'Delitto e Castigo'), 'IN_LETTURA', NULL, '2026-06-10');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Bianchi'), (SELECT id FROM libro WHERE titolo = '1984'), 'DA_LEGGERE', NULL, '2026-07-01');

-- --- Marco: 3 libri di Christie valutati 4-5 stelle + 1 di Doyle -> ci si aspetta che
--     "Nemesi" (Christie, il suo autore più votato) sia il primo consiglio per autore
--     preferito, e altri titoli Giallo (Doyle, Highsmith) come consiglio per genere
--     preferito. Include anche un libro non valutato di un genere diverso.
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Assassinio sull''Orient Express'), 'COMPLETATO', 5, '2026-04-15');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Dieci Piccoli Indiani'), 'COMPLETATO', 4, '2026-05-05');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Poirot a Styles Court'), 'COMPLETATO', 5, '2026-05-25');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Uno Studio in Rosso'), 'COMPLETATO', 4, '2026-06-08');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Verdi'), (SELECT id FROM libro WHERE titolo = 'Fondazione'), 'DA_LEGGERE', NULL, '2026-07-10');

-- --- Giulia: 2 libri di Orwell (Distopico) valutati 5, 1 libro di Harari (Saggistica)
--     valutato 5, e 1 libro di Asimov valutato SOLO 3 (sotto la soglia 4+, non deve
--     contare come preferenza). Ci si aspetta che "Omaggio alla Catalogna" (Orwell,
--     Saggistica) venga consigliato per autore preferito ANCHE SE il genere del libro non
--     è il suo genere preferito principale (Distopico) - dimostra che il consiglio per
--     autore scavalca correttamente i confini di genere.
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Rossi'), (SELECT id FROM libro WHERE titolo = '1984'), 'COMPLETATO', 5, '2026-03-10');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Rossi'), (SELECT id FROM libro WHERE titolo = 'La Fattoria degli Animali'), 'COMPLETATO', 5, '2026-03-20');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Rossi'), (SELECT id FROM libro WHERE titolo = 'Sapiens. Da Animali a Dei'), 'COMPLETATO', 5, '2026-04-02');
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Rossi'), (SELECT id FROM libro WHERE titolo = 'Io, Robot'), 'COMPLETATO', 3, '2026-04-20');

-- --- Paolo: un solo libro "da leggere", nessuna valutazione -> caso limite: la lista dei
--     consigli per Paolo deve risultare VUOTA, a dimostrazione che il motore non propone
--     libri generici quando non ha ancora dati sufficienti sui gusti dell'utente.
INSERT INTO elemento_libreria (id, utente_id, libro_id, stato_lettura, valutazione, data_aggiunta) VALUES (nextval('elementolibreria_seq'), (SELECT id FROM users WHERE cognome = 'Ferrari'), (SELECT id FROM libro WHERE titolo = 'Le Città Invisibili'), 'DA_LEGGERE', NULL, '2026-07-15');
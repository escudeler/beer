CREATE DATABASE if not EXISTS craftbeerdb;

CREATE TABLE if not EXISTS beer ( id INTEGER NOT NULL AUTO_INCREMENT, name VARCHAR(50) NOT NULL, ingredients VARCHAR(150) NOT NULL, alcohol_content VARCHAR(20) NOT NULL, price NUMERIC(15,2) NOT NULL, category VARCHAR(50) NOT NULL, PRIMARY KEY (id) );


INSERT INTO beer (id, name, ingredients, alcohol_content, price, category) VALUES
(1, 'Bruxelas Juice test', 'Durst Pilsner malt, Dingemans Cara45 malt, Kent Golding pellet hops, Wyeast 3655 Fermenter', '4.8 - 5.5%', '10.00', 'Pale Ale'),
(2, 'Goose Island IPA', '�gua, maltes especiais e l�pulo', '5.9%', '8.45', 'India Pale Ale'),
(3, 'Black Anthrax - Coffea', 'vers�o coffea (caf�) conta com adi��o de baunilha, carvalho e 400 caf�s expressos do Virginia Coffee Roasters tirados e adicionados 1 a 1 na cerveja', '15.4%', '59.90', 'Imperial Stout'),
(4, 'Black Anthrax - Nucifera', 'vers�o nuficera (coconut) contem adi��o de baunilha, caf�, carvalho e coco', '15.4%', '59.90', 'Imperial Stout'),
(5, 'Suricato - Pulp', 'Arom�tica com corpo aveludado e amargor bem inserido tem notas de frutas tropicais como abacaxi, manga, p�ssego, mel�o e maracuj�.', '6.9%', '44.00', 'New England IPA');

# Käyttöohje

lataa ensin [jar-tiedosto](https://github.com/TuuPu/ot-harjoitustyo/releases/tag/viikko6)

## Ohjelman käynnistäminen
Ohjelma käynnistyy komennolla mvn compile exec:java -Dexec.mainClass=productivitytracker.Main.
MacOS-käyttöjärjestelmällä käynnistäminen ja testit ovat suoriutuneet ongelmitta. Cublilla kokeiltaessa compile ei aina jostain syystä toiminut, vaan vaati mvn clean & mvn compile komennot ennen edellämainittua käynnistyskomentoa. Mutta ohjelma kuitenkin toimii moitteetta laitoksen koneilla.
Ohjelman voi myös suorittaa yllämainitulla jar-tiedostolla.

## Ohjelman käyttö
Ohjelman käynnistyessä käyttäjälle avautuu seuraava näkymä:
![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-05-04%20at%2017.18.49.png)

"Start studying" -nappia painamalla opiskelija voi lisätä haluamansa kurssin tiedot ohjelmalle
"Start a session" -nappia painamalla opiskelija pääsee "träkkäämään" valittua kurssia
"Exit" -nappi sulkee ohjelman

## Start studying

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-05-04%20at%2017.19.26.png)
Yllä oleva näkymä on kuva kurssin lisäyksen näkymästä. Kentät vaativat kurssin nimen, opintopistemäärän, sekä tavoiteajan päivittäiselle opiskelulle.

## Start a session

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-05-04%20at%2017.19.51.png)
Start a session napin takana on seuraava näkymä. Vasemmassa yläkulmassa on dropdown-valikko, josta tietokantaan tallennetut kurssit on mahdollista valita. Ohjelma ilmoittaa käyttäjälleen mikä kurssi on seurattavana tällä hetkellä. Start napista opiskelusessio alkaa ja ohjelma kertoo kurssin nimen vielä kertaalleen ja kertoo myös kurssille asetetun tavoiteajan.

Lisäksi näkymä kertoo myös viikonpäiville kertyneet total-ajat kaikelle opiskelulle kurssin nimestä huolimatta.

Stop nappia painaessa ohjelma kertoo kyseisen session prosentuaalisen etenemisen tavoiteaikaan nähden. Lisäksi saat näkyville uuden taulukon, joka kertoo keskimääräisen opiskeluajan suhteen tavoiteajalle per. viikonpäivä kyseiselle kurssille.

Lisäksi ruutuun tulee ilmoitus, jos opiskelijan opiskelusessio on jatkunut puolen tunnin ajan, kehoittaen tauon pitämiseen. Lisäksi stop-nappia painaessa *jos* opiskelusessioiden kesto suhteessa yhteen opintopisteeseen valitulle kurssille näyttää ylittävän 27 tuntia tulee varoitus.

Statistics napista pääset seuraavaan näkymään:

## Statistics

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-05-04%20at%2017.20.17.png)

Näyttää käyttäjälle erilaisia graafisia statistisia esityksiä opintojen etenemisestä.

## Huomionarvoista!

Testit, jotka tarkastavat prosenttiarvojen oikeaa esitystapaa onnistuvat, kun testit tehdään cubblilla, muttta epäonnistuvat macOS-käyttöjärjestelmällä. Tämä johtuu siitä, että prosenttiarvot palautetaan String-muodossa ja ne on asetettu toimimaan Cubblin mukaan, joka palauttaa 00.00 tyyppisen arvon. MacOS:llä testatessa palautettava arvo on muotoa 00,00.

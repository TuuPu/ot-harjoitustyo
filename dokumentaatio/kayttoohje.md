# Käyttöohje

## Ohjelman käynnistäminen
Ohjelma käynnistyy komennolla mvn compile exec:java -Dexec.mainClass=productivitytracker.Main.
MacOS-käyttöjärjestelmällä käynnistäminen ja testit ovat suoriutuneet ongelmitta. Cublilla kokeiltaessa compile ei aina jostain syystä toiminut, vaan vaati mvn clean & mvn compile komennot ennen edellämainittua käynnistyskomentoa. Mutta ohjelma kuitenkin toimii moitteetta laitoksen koneilla.

## Ohjelman käyttö
Ohjelman käynnistyessä käyttäjä voi valita numeron välillä 1...6. Numeroidut komennot johtavat seuraaviin toimintoihin:

Valitsemalla 1 käyttäjä voi *luoda* uuden kurssin, esim. ohte. Tämän jälkeen käyttäjää pyydetään asettamaan opintopistemäärä kyseiselle kurssille, jonka jälkeen käyttäjää ohjeistetaan ilmoittamaan hänen oma tavoiteaika kyseisen kurssin opiskelulle per. päivä.

Valitsemalla 2 käyttäjä voi valita *jo luoduista* kursseista haluamansa seurattavaksi.

Valitsemalla 3 käyttäjä käynnistää ajastimen ja aloittaa opintojen seurannan *valitulle* kurssille.

Valitsemalla 4 käyttäjä lopettaa aloitetun seurannan ja saa tietoonsa sen prosentuaalisen etenemisen tavoitteeseensa nähden.

Valitsemalla 5 käyttäjä saa mahdollisuuden valita alavalikossa "A", "B" tai "C" ja näkee opiskeluajoistaan tarkempaa dataa. Nykyisessä versiossa opiskelija näkee totaaliaikojen lisäksi keskimääräisen opiskeluajan, mediaanin opiskeluajalle, sekä keskihajonnan opiskeluajalle. Tietyn kurssin tarkastelun valitessaan, opiskelija näkee lisäksi keskimääräisen opiskelun prosentuaalisen arvon suhteessa tavoiteaikaan per. viikonpäivä.

Valitsemalla 6 ohjelma lopetetaan ja database suljetaan.

## Huomionarvoista!

Testit, jotka tarkastavat prosenttiarvojen oikeaa esitystapaa onnistuvat, kun testit tehdään cubblilla, muttta epäonnistuvat macOS-käyttöjärjestelmällä. Tämä johtuu siitä, että prosenttiarvot palautetaan String-muodossa ja ne on asetettu toimimaan Cubblin mukaan, joka palauttaa 00.00 tyyppisen arvon. MacOS:llä testatessa palautettava arvo on muotoa 00,00.

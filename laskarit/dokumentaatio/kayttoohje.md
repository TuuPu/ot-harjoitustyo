# Käyttöohje

## Ohjelman käynnistäminen
Ohjelma käynnistyy komennolla mvn compile exec:java -Dexec.mainClass=productivitytracker.Main.
MacOS-käyttöjärjestelmällä käynnistäminen ja testit ovat suoriutuneet ongelmitta. Cublilla kokeiltaessa compile ei aina jostain syystä toiminut, vaan vaati mvn clean & mvn compile komennot ennen edellämainittua käynnistyskomentoa. Mutta ohjelma kuitenkin toimii moitteetta laitoksen koneilla.

## Ohjelman käyttö
Ohjelman käynnistyessä käyttäjä voi valita numeron välillä 1...5. Numeroidut komennot johtavat seuraaviin toimintoihin:
Valitsemalla 1 käyttäjä voi *luoda* uuden kurssin, esim. ohte. Tämän jälkeen käyttäjää pyydetään asettamaan opintopistemäärä kyseiselle kurssille, jonka jälkeen käyttäjää ohjeistetaan ilmoittamaan hänen oma tavoiteaika kyseisen kurssin opiskelulle per. päivä.
valitsemalla 2 käyttäjä voi valita *jo luoduista* kursseista haluamansa seurattavaksi.
valitsemalla 3 käyttäjä käynnistää ajastimen ja aloittaa opintojen seurannan *valitulle* kurssille.
valitsemalla 4 käyttäjä lopettaa aloitetun seurannan ja saa tietoonsa sen prosentuaalisen etenemisen tavoitteeseensa nähden.
valitsemalla 5 ohjelma lopetetaan.

## Huomionarvoista!

Testit, jotka tarkastavat prosenttiarvojen oikeaa esitystapaa onnistuvat, kun testit tehdään cubblilla, muttta epäonnistuvat macOS-käyttöjärjestelmällä. Tämä johtuu siitä, että prosenttiarvot palautetaan String-muodossa ja ne on asetettu toimimaan Cubblin mukaan, joka palauttaa 00.00 tyyppisen arvon. MacOS:llä testatessa palautettava arvo on muotoa 00,00.

# Arkkitehtuurikuvaus

## Rakenne

Ohjelman rakenne noudattaa kaksitasoista kerrosarkkitehtuuria. Toistaiseksi pakkaus productivitytracker.ui kutsuu pakkauksia productivitytracker.course, productivitytracker.statistics ja productivitytracker.ptdatabase, mutta viimeisessä julkaisussa edellämainitut kolme pakkausta on tarkoitus asettaa kaikki pakkauksen productivitytracker.service alle.

Pakkaus productivitytracker.ui sisältää JavaFX:llä luodun käyttöliittymän, joka vastaa käyttäjän kanssa kommunikoinnista. productivitytracker.ptdatabase sisältää tietokannan ja tietojen pysyväistallennuksen, ja productivitytracker.course, sekä producitivitytracker.statistics sovelluksen logiikasta vastaavia osia.

## Käyttöliittymä

Käyttöliittymä sisältää neljä erilaista näkymää:

* Päänäkymä

* Kurssien luomisen tietokantaan

* Kurssien seurantaan liittyvän näkymän

* Statistiikan visuaalisen esityksen näkymän

Jokainen näistä on toteutettu omana Scene-oliona ja näkymät ovat yksi kerrallaan esillä.

Käyttöliittymä sisältää toistaiseksi joitain sovelluslogiikan osia, kuten mm. metodin loadClasses(). Nämä on tarkoitus siirtää viimeisellä viikolla, ennen lopullista julkaisua Service-nimiseen luokkaan selkeytyksen vuoksi.


## Sovelluslogiikka

Sovelluksen logiikka pohjautuu Course, Statistics ja PTDatabase luokkien yhteistoimintaan, jossa Course kuvaa eri opiskeltavien kurssien ilmentymiä, joilla on tiedot. PTDatabase tallentaa nämä tiedot ja Statistics käsittelee niitä matemaattisin keinoin.

Toistaiseksi jokaista oliota kutsutaan tarvittaessa käyttöliittymän sisällä, mutta kuten aiemmin mainittu, niiden metodit on tarkoitus kasata yhteen Service-luokkaan, jonka avulla logiikka saadaan keskitettyä yhteen luokkaan. Tulevat Service-luokan metodit ovat vastaavia, kun luokkien omat metodit, kuten:

* getMean(String type, int identifier) <- type esim. "tira" ja identifier 0. Tällä metodi tietää hakea kurssin tira tietoja.
* setPoints(String course) <- asettaa valitulle kurssille opintopisteet

## Tietojen pysyväistallennus

Pakkauksen productivitytracker.ptdatabase luokka PTDatabase vastaa tietojen pysyväistallennuksesta.

Luokka sisältää kaksi taulua: Courses ja Daily. Taulu Courses sisältää kurssin tiedot, jotka ovat:

* Kurssin nimi
* Kurssin opintopistemäärä
* Tavoiteaika opiskelulle
* Arvosana

Taulu Daily sisältää kurssin opiskelusessioihin liittyviä tietoja ja linkittyy tauluu Courses course_id kolumin avulla:

* Päivämäärä opiskelulle
* course_id
* Viikonpäivä
* Kurssin nimi
* Opiskelusession kesto

Tauluissa on varmistettu, että käyttäjä ei voi jättää syöttämättä kriittisiä tietoja, kuten kurssin nimeä, jotta sen tietoja voidaan myöhemmin hakea. Parametreina taulujen tietotyypit ovat selkeitä, joten käyttäjälle ei anneta tiukkoja ehtoja symboleista tai merkeistä, joita hän voi käyttää.


## Päätoiminnallisuudet

Alla on luokkakaavio pääpiirteisesti ohjelman osien suhteista ja kuvaus opiskelusession aloittamisesta sekvenssikaavion muodossa.
Huomioitavaa tässä on se, että kurssi, jonka opiskelusessio aloitetaan on pitänyt ensin syöttää ohjelmalle tietoineen. Eli nykyinen opiskeltava kurssi on siis luokan Course tiedossa. Session loppuessa ne siirretään PTDataBase-luokan tietoihin. Tämän takia aivan oikeassa alakulmassa olevat getterit kurssin ja tietokannan välillä voivat vaikuttaa ensisilmäykseltä hieman oudoilta.
Kuvaus tulee vielä muuttumaan hieman viimeisessä palautuksessa, koska pakettien rakenne tulee vielä hieman muuttumaan.

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-04-27%20at%2019.06.10.png)

## Luokkakaavio

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-05-04%20at%2016.40.57.png)

## Ohjelman rakenteeseen jääneet heikkoudet

### kolmen kerroksen arkkitehtuuri

Järkevin ratkaisu olisi ollut rakentaa koko ohjelma ui -> service -> logic rakenteella, nyt loppupalautus tulee olemaan kaksitasoinen ui -> service tyylinen. Tämä rakenteellinen puutos johtui siitä, että rakensin pakkaukset suoraan productivitytracker pakkauksen alle aluksi. Kiireen vuoksi en ymmärtänyt tai ehtinyt tarkastella järkevämpää sovelluksen rakennetta ja se jäi puutteelliseksi. Viimeisen viikon muutos pyrkii korjaamaan tätä virhettä edes hieman.

### Metodit

Metodit olisivat voineet olla pilkottuna vieläkin suurempiin paloihin ja tähänkin olisi auttanut kolmitasoinen rakenne. Nyt osa luokkien metodeista hoitaa paria asiaa kerralla. Mutta olen pyrkinyt minimoimaan näitä metodeita. Esimerkiksi Statistics luokassa getterit hakevat tiedot ja laskevat suoraan esimerkiksi keskiarvon. Samalla tavalla tietokanta-luokan metodit saattavat hakea tietoa ja tehdä kevyen laskun ja asettaa tiedot valmiiseen hajautustauluun tai listaan.

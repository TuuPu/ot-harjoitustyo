# Arkkitehtuurikuvaus

## Rakenne

Ohjelman rakenne noudattaa kaksitasoista kerrosarkkitehtuuria. Luokka UserInterface kutsuu Service-luokkaa, joka toimii toiminnallisuus-yksikkönä muille luokille, jotka ovat PTDataBase, Course ja Statistics. Siis Service luokka kasaa kaikkien luokkien toiminnallisuuden yhteen, jotta käyttöliittymässä toiminnallisuuksien käsittely olisi suoraviivaisempaa.

Pakkaus productivitytracker.ui sisältää JavaFX:llä luodun käyttöliittymän, joka vastaa käyttäjän kanssa kommunikoinnista. productivitytracker.ptdatabase sisältää tietokannan ja tietojen pysyväistallennuksen, ja productivitytracker.course, sekä producitivitytracker.statistics sovelluksen logiikasta vastaavia osia. Service kokoaa sekä pysyväistallennuksen ja loogisen osan ohjelmasta yhteen.

## Käyttöliittymä

Käyttöliittymä sisältää neljä erilaista näkymää:

* Päänäkymä

* Kurssien luomisen tietokantaan

* Kurssien seurantaan liittyvän näkymän

* Statistiikan visuaalisen esityksen näkymän

Jokainen näistä on toteutettu omana Scene-oliona ja näkymät ovat yksi kerrallaan esillä.



## Sovelluslogiikka

Sovelluksen logiikka pohjautuu Course, Statistics ja PTDatabase luokkien yhteistoimintaan, jossa Course kuvaa eri opiskeltavien kurssien ilmentymiä, joilla on tiedot. PTDatabase tallentaa nämä tiedot ja Statistics käsittelee niitä matemaattisin keinoin.

Kaikki yllämainitut luokat ovat koottu yhteen Service-luokan alle, jossa niiden metodeja kutsutaan ja yhdistellään. Näin saadaan koko sovelluslogiikka "yksien kirjojen" alle ja sen käyttö on yksinkertaisempaa.


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

Alla on luokkakaavio pääpiirteisesti ohjelman osien suhteista. Päätoiminnallisuuden ideana on se, että ui-luokka kutsuu Service-luokkaa tarvittaessa, joka tarjoaa metodit jokaisen logiikasta vastaavan luokan toiminnallisuuksiin. Pyrkimyksenä on käyttää kursseista vastaavaa luokkaa vain silloin, jos uusi kurssi luodaan. Muutoin kaikki toiminnalisuudet tapahtuvat tietokannan, statistiikan ja service-luokan välillä. 

## Luokkakaavio

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/uusiLuokka.png)

## Ohjelman rakenteeseen jääneet heikkoudet

### kolmen kerroksen arkkitehtuuri

Järkevin ratkaisu olisi ollut rakentaa koko ohjelma ui -> service -> logic rakenteella, nyt loppupalautus tulee olemaan kaksitasoinen ui -> service tyylinen. Tämä rakenteellinen puutos johtui siitä, että rakensin pakkaukset suoraan productivitytracker pakkauksen alle aluksi. Kiireen vuoksi en ymmärtänyt tai ehtinyt tarkastella järkevämpää sovelluksen rakennetta ja se jäi puutteelliseksi. Viimeisen viikon muutos pyrkii korjaamaan tätä virhettä edes hieman.

### Metodit

Metodit olisivat voineet olla pilkottuna vieläkin suurempiin paloihin ja tähänkin olisi auttanut kolmitasoinen rakenne. Nyt osa luokkien metodeista hoitaa paria asiaa kerralla. Mutta olen pyrkinyt minimoimaan näitä metodeita. Esimerkiksi Statistics luokassa getterit hakevat tiedot ja laskevat suoraan esimerkiksi keskiarvon. Samalla tavalla tietokanta-luokan metodit saattavat hakea tietoa ja tehdä kevyen laskun ja asettaa tiedot valmiiseen hajautustauluun tai listaan.

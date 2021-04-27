## Luokkakaavio

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/laskarit/dokumentaatio/kuvat/ProductivityLuokkakaavio.PNG)

Toistaiseksi vain kuvaus luokkien suhteesta ilman luokkien sisältöjen tarkempaa esittelyä.

## Sekvenssikaavio

![alt text](https://github.com/TuuPu/ot-harjoitustyo/blob/master/dokumentaatio/kuvat/Screenshot%202021-04-27%20at%2019.06.10.png)

Sekvenssikaaviossa esitellään ohjelman toiminnallisuutta, kun käyttäjä päättää aloittaa opiskelusession. Huomioitavaa tässä on se, että kurssi, jonka opiskelusessio aloitetaan on pitänyt ensin syöttää ohjelmalle tietoineen. Eli nykyinen opiskeltava kurssi on siis luokan Course tiedossa. Session loppuessa ne siirretään PTDataBase-luokan tietoihin. Tämän takia aivan oikeassa alakulmassa olevat getterit kurssin ja tietokannan välillä voivat vaikuttaa ensisilmäykseltä hieman oudoilta.

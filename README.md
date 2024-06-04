# Alkohola Cenu Salīdzināšanas Lietotne

Šī ir Android lietotne, kas ļauj lietotājiem salīdzināt alkohola cenas dažādos veikalos. Lietotne izmanto Firebase autentifikāciju un veic HTTP pieprasījumus uz privāto API, lai iegūtu datus par produktiem un kategorijām.

## Linki uz prezentāciju un dokumentāciju

- Prezentācija:
    ```bash
    https://docs.google.com/presentation/d/1yxwDvqrMxUgq_3rKD-3FV5qVTxQFH-_aK4gh0Gmn5Do/edit#slide=id.g2e277a1b907_0_87
    ```
- Dokumentācija:
    ```bash
    https://docs.google.com/document/d/10V_kYWaIxQHPpxmJU34Z91HgTPrECkVcSpdCxE8Dyjw/edit#heading=h.wt7stc4qlmjv
    ```

## Funkcionalitāte

- Lietotāju autentifikācija, izmantojot Firebase (reģistrācija un pieslēgšanās).
- Produktu meklēšana.
- Kategoriju apskate.
- Produkta informācijas apskate, ieskaitot veikalus un cenas, kurās tas pieejams.

## Ekrāni

1. **Sākotnējais Ekrāns (`activity_firstmeet.xml`)**: Tiek rādīts, ja lietotājs nav autorizēts vai reģistrēts.
2. **Galvenais Ekrāns (`activity_main.xml`)**: Ekrāns ar izvēlni, kur lietotājs var izvēlēties starp pieslēgšanos un reģistrāciju.
3. **Reģistrācijas Ekrāns (`activity_register.xml`)**: Reģistrācijas forma.
4. **Pieslēgšanās Ekrāns (`activity_login.xml`)**: Pieslēgšanās forma.
5. **Sveiciena Ekrāns (`activity_hello.xml`)**: Tiek rādīts pēc veiksmīgas reģistrācijas.
6. **Vispārējās Izvēlnes Ekrāns (`activity_general_menu.xml`)**: Ekrāns ar produktu meklēšanas iespēju.
7. **Produktu Saraksta Ekrāns (`activity_products.xml`)**: Meklēšanas rezultātu un kategoriju produktu saraksts.
8. **Produkta Informācijas Ekrāns (`activity_product.xml`)**: Detalizēta informācija par izvēlēto produktu.

## Instalācija

1. Klonējiet repozitoriju:
    ```bash
    git clone https://github.com/SuspectWorkers/AndroidClient.git
    ```
2. Atveriet projektu Android Studio.
3. Pārliecinieties, ka ir pievienotas visas nepieciešamās atkarības `build.gradle` failā.
4. Sāciet projektu ar Android Studio.

## Izmantotās Bibliotēkas

- **Firebase**: Autentifikācijai un Firestore datu glabāšanai.
- **Retrofit**: HTTP pieprasījumu veikšanai.
- **Picasso**: Attēlu ielādei.
- **AndroidX**: Modernām Android funkcionalitātēm un UI komponentiem.

## Kods Piemēri

### Autentifikācija

```kotlin
FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
    .addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Reģistrācija veiksmīga
        } else {
            // Reģistrācija neizdevās
        }
    }

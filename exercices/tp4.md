# Exercices - TP4

# Stories

## **Story: Créer un avis sur un restaurant**

En tant que client, je devrais pouvoir laisser une note à un restaurant que j’ai visité ou non.

### **Critères d’acceptation:**

- **Requête :** POST /restaurants/reviews
- **Corps de la requête:**

    ```json
    {
    "restaurantId": "12345",
    "clientId": "67890",
    "rating": 4,
    "timestamp": "2024-04-10"
    }
    ```

- **Réponse :**
    - **201 Created**: La requête me retourne un statut 201 created pour me montrer que ma note a bel et bien été attribuée au restaurant en question.
    - **400 Bad Request**: Par exemple si la requête a une date qui n’est pas encore arrivée
    - **404 Not Found:** le restaurant spécifié n'existe pas.

---

## **Open Source**
### **Questions**

**1. Nommez 3 avantages à contribuer à des projets open source en tant qu'entreprise et justifiez en quoi cela peut être bénéfique pour tous.**

En tant qu'entreprise, contribuer à des projets open source peut être bénéfique pour plusieurs raisons. Tout d'abord, 
cela permet de réduire les coûts de développement en utilisant des solutions déjà existantes et en les adaptant à ses 
besoins. De plus, une grande partie de contributeurs sont des gens très bien formés ayant des compétences poussées dans
le domaine, ce qui permet de bénéficier de leur expertise sans avoir à les embaucher. Cela permet donc de gagner du
temps et de l'argent pour l'entreprise qui peut se concentrer sur d'autres aspects de son activité.

Ensuite, avoir un produit open source permet de laisser libre cours à la communauté et aux utilisateurs du produit de 
trouver et rajouter des fonctionnalités et cas d'utilisation précis et qui peuvent être très intéréssants 
pour le produit final. Cela permet d'avoir un produit plus complet et plus adapté aux besoins des utilisateurs.

Enfin, cela permet de renforcer la réputation de l'entreprise en tant qu'acteur engagé dans la communauté open source.
Cela peut attirer des talents et des clients qui sont sensibles à ces valeurs et qui veulent travailler avec des
entreprises qui partagent leurs valeurs. Cela peut également permettre de se démarquer de la concurrence et de gagner
en visibilité sur le marché. Par exemple, parmi les compagnies utilisant le plus l'open source on peut facilement en 
citer plusieurs qui sont très populaires et qui ont une très bonne réputation, comme Google, Facebook et Microsoft.

**2. Décrivez 3 défis qu'impose la mise en place d'un projet open source et justifiez.**

La mise en place d'un projet open source peut poser plusieurs défis. Tout d'abord, il faut s'assurer que le code est
de qualité et bien documenté pour faciliter la contribution de la communauté. Cela permet également d'avoir des 
contributions de meilleure qualité et de diminuer les issues de support, car certaines choses dans le code pourraient 
être incomprises du public. Il est impératif que les mainteneurs du projet énoncent clairement leur vision pour le 
projet et les standards de qualité à respecter pour que les contributions soient acceptées et intégrées dans le projet.

Ensuite, il faut gérer les issues et les contributions de la communauté pour s'assurer qu'elles sont à jour et conformes 
aux standards de l'entreprise. En effet, une liste de issues à jour permet non seulement d'attirer de nouveaux 
contributeurs qui peuvent observer le sérieux du projet et le suivi effectué, mais aussi de maintenir un contrôle 
intellectuel sur le projet malgré un grand nombre de contributeurs. Avoir de la documentation qui permet de comprendre 
quels sont les différents rôles au sein du répértoire et les différents droits et pouvoirs de chacun permet d'avoir une 
meilleure clarté. La communication se doit également d'être publique et documentée pour éviter les malentendus et les 
conflits.

Enfin, l'instauration d'un code de conduite que ce soit pour les événements associés au projet, les commits des 
contributeurs ou au niveau de la communication permet grandement au mainteneurs du projet de maintenir un contrôle et 
surtout de les aider à prendre des décisions en cas de conflit ou de problème. Cela permet également de créer un
environnement sain et inclusif pour la communauté open source. En effet, déjà qu'il est très difficile pour un 
mainteneur de dire Non à un contributeur, avoir des règles claires et des conséquences pré-établies permet de 
faciliter la tâche du mainteneur à prendre des décisions dans certaines situations.

**3. Quelle information vous a-t-elle le plus surprise à propos de l'open source?**

Parmi les choses qui nous ont le plsu impressionné sur l'open source est le fait qu'il paraît que le open source est 
tout autant, si ce n'est plus sécuritaire que le code propriétaire. En effet, le fait que le code soit ouvert permet à
la communauté de le regarder et de le tester, ce qui permet de trouver et de corriger les failles de sécurité plus
rapidement. De plus, le fait que le code soit ouvert permet également de vérifier que le code ne contient pas de
backdoors ou de failles intentionnelles, ce qui peut être un risque avec le code propriétaire. Cela permet donc de
renforcer la sécurité du code et de réduire les risques pour les utilisateurs. Cela peut être un argument de poids pour
convaincre les entreprises de passer à l'open source et de contribuer à des projets open source.

[Top 10 Misconceptions about open source software]
(https://www.hotwaxsystems.com/hotwax-blog/ofbiz/the-top-10-misconceptions-about-open-source-software)

### **Notre Code de conduite**

Notre code de conduite est adapté de la [Contributor Covenant]
(https://www.contributor-covenant.org/version/1/4/code-of-conduct.html) version 1.4. Nous avons choisi ce modèle
principalement, car il est très complet et qu'il est utilisé par de nombreux projets open source. Il est important pour
nous de créer un environnement sain et inclusif pour notre communauté open source, et ce code de conduite nous permet
de poser des bases solides pour cela. Nous avons également ajouté quelques éléments spécifiques à notre projet pour
répondre à nos besoins spécifiques et à notre vision du projet.

### **License**

Nous avons choisi la licence MIT pour notre projet open source. Nous avons choisi cette licence principalement, car elle
est très permissive et permet à quiconque d'utiliser, de modifier et de distribuer notre code sans restrictions. De plus, la licence
MIT est très populaire et bien connue, ce qui facilite la compréhension et l'acceptation de notre projet par les
utilisateurs et les contributeurs qui voudraient potentiellement l'utiliser. Enfin, la licence MIT est compatible avec 
de nombreuses autres licences open source, ce qui permet de faciliter l'intégration de notre projet dans d'autres 
projets open source dans le futur. Dans notre cas, il n'a pas été nécessaire pour nous de choisir une license comme 
Apache 2.0, car nous ne sommes pas une entreprise et nous n'avons pas de brevets à protéger. Nous n'avions pas non plus
à utiliser une license GPL, car nous ne voulions pas imposer de restrictions sur l'utilisation du code par les
utilisateurs et les contributeurs.

[The Legal Side Of Open Source](https://opensource.guide/legal/#which-open-source-license-is-appropriate-for-my-project)


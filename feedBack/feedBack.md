# Introduction 

# Échéancier

# Glossaire 
- Application MaVille.
- Info entraves et Travaux.
- Type de problème, signaler un problème.

Les termes mentionnés ci-dessus sont des termes importants qui devraient être dans votre glossaire mais qui sont manquants.

- Authentification.
- Formulaire.
- Validation.

Les termes mentionnés ci-dessus sont jugés non pertinents pour votre glossaire. Les termes de votre glossaire doivent être en relation avec la description de votre projet et présents dans la description du rapport.

# Diagramme de cas d'utilisation 
### Respect du formalisme 
- Le formalisme des diagrammes de CU est bien respecté.
### Identification des acteurs 
- L'acteur **serveur** n'est pas nécessaire et n'est visible dans aucun CU du diagramme.
- On aurait pu créer une généralisation entre le **résident** et l'**intervenant** et leur attribuer les CUs **inscription** et **authentification**.
- L'acteur secondaire **ville** est manquant : il aurait pour rôle d'aider à la réalisation de certains CUs comme : **s'inscrire comme intervenant**, **signaler un problème**.
### Cas d'utilisation 
- **Répondre à une candidature** :
    - Si on soumet une requête, on doit pouvoir accepter ou refuser les candidatures des intervenants.
- **Soumettre sa candidature** :
    - Un intervenant doit pouvoir soumettre sa candidature pour les requêtes faites par les résidents.

# Scénarios 
- **S'authentifier comme résident** :
    - Précondition :
        - Pas besoin de mentionner **Aucune précondition pour l'inscription** car le CU concerne l'authentification et non l'inscription.
    - Pour l'étape 2, ce n'est pas le **serveur** qui va afficher l'écran d'authentification, mais le **système**.
    - L'étape 8 n'est pas pertinente, ce n'est pas une action effectuée par l'intervenant ou le résident.
    - Le scénario de l'étape 3 alternatif n'est pas pertinent, le but du scénario est l'authentification. On suppose donc que l'utilisateur sait ce qu'il veut faire.

- **Consulter les travaux** :
  - La postcondition est incorrecte, car on veut consulter les travaux et non un travail en particulier. On pourrait donc avoir la postcondition suivante : **la liste des travaux est bien affichée**.
  - **Le résident demeure sur l'onglet actuel** : cette étape n'est pas pertinente, on veut juste décrire les étapes faites par l'utilisateur ou le système pour réaliser le CU.
  - On souhaite juste consulter les travaux, donc toutes les étapes après l'étape 3 ne sont pas nécessaires.
  - Les CUs **sélectionner notification** et **rechercher travaux** doivent être mis comme scénarios alternatifs et donc ne doivent pas se trouver dans le scénario principal, car ces CUs étendent le CU principal.
  - Il y a un souci de compréhension de l'utilité des scénarios alternatifs. Les scénarios alternatifs permettent de gérer les extensions (**<< extends >>**), les erreurs, etc., mais ne permettent pas de gérer les boutons cliqués par l'utilisateur lors de son interaction avec notre interface.

- **Soumission d'une requête de travail** :
    - L'étape 4 **Le résident ajoute des pièces jointes si nécessaire** : on spécifie juste une étape que l'on est sûr qu'elle sera faite par l'utilisateur ou par le système. Le **si nécessaire** nous dit donc que ce n'est pas sûr que cette action soit effectuée par l'utilisateur.
    - Le scénario 6a n'affecte pas le point 6, mais aurait dû affecter le point 4.

- **Suivi des requêtes de travail** :
    - L'étape 5 n'est pas cohérente avec le diagramme de CU. Pour que cette étape soit cohérente, il aurait fallu que **accepter une candidature** soit inclus dans ce CU.
    - À cause du point précédent, le point 6 est donc inutile.

- **Envoyer ses préférences de plage horaire** :
    - La postcondition n'est pas bien formulée, car la postcondition nous indique l'état du système après l'interaction avec l'utilisateur.
    - Problème avec le scénario alternatif :
        - Le 3a n'est pas nécessaire, comme expliqué précédemment.

- **Envoyer son avis sur les travaux terminés** :
    - Postcondition reformulée en **L'avis du résident a bien été envoyé**.
    - Problème avec le scénario alternatif :
        - Le 5a n'est pas nécessaire, comme expliqué précédemment.

- **Signaler un problème** :
    - Postcondition : **Le problème a bien été soumis**.
    - L'étape 4 **Le résident ajoute des pièces jointes si nécessaire** : on spécifie juste une étape que l'on est sûr qu'elle sera faite par l'utilisateur ou par le système. Le **si nécessaire** nous dit donc que ce n'est pas sûr que cette action soit effectuée par l'utilisateur.
    - Le scénario alternatif est problématique, comme mentionné précédemment.

- **Accéder aux notifications** :
    - Toutes les étapes après l'étape 2 ne sont pas nécessaires, car à l'étape 2 notre objectif est réalisé.
    - Le cas alternatif 5b n'est pas nécessaire.

- **S'authentifier comme intervenant** :
    - Précondition :
        - Le CU est l'authentification de l'intervenant, donc dire dans la précondition **Si l'intervenant se connecte** ou encore **Si l'intervenant s'inscrit** est problématique.
    - Vu que le but de ce CU est de s'authentifier, l'utilisateur ne va donc pas choisir de cliquer sur le bouton **S'inscrire**, un autre CU doit gérer cette action, sauf si **S'authentifier comme intervenant** est étendu par le CU **S'inscrire comme intervenant**.

- **Mettre à jour les informations d'un chantier** :
    - Précondition :
        - Vous ne pouvez pas mettre en précondition le fait que l'intervenant soit connecté, car **Mettre à jour les informations d'un chantier** inclut **S'authentifier comme intervenant**, donc il est obligatoire de l'avoir dans votre scénario principal, ce qui n'est donc pas cohérent avec votre diagramme de CU.
    - Scénario alternatif :
        - Même problème de scénario alternatif que mentionné précédemment.

- **Soumettre un nouveau travail** :
    - Précondition :
        - Vous ne pouvez pas mettre en précondition le fait que l'intervenant soit connecté, car **Soumettre un nouveau travail** inclut **S'authentifier comme intervenant**, donc il est obligatoire de l'avoir dans votre scénario principal, ce qui n'est donc pas cohérent avec votre diagramme de CU.
    - Problème dans les étapes : l'intervenant peut soumettre un nouveau travail sans forcément consulter les préférences des utilisateurs.
    - Scénario alternatif :
        - Vous avez donc le même problème avec les scénarios alternatifs 4a et 9c, comme les problèmes précédemment mentionnés.

- **Consulter la liste des requêtes de travail** :
    - Précondition :
        - Vous ne pouvez pas mettre en précondition le fait que l'intervenant soit connecté, car **Consulter la liste des requêtes de travail** inclut **S'authentifier comme intervenant**, donc il est obligatoire de l'avoir dans votre scénario principal, ce qui n'est donc pas cohérent avec votre diagramme de CU.
    - Postcondition :
        - La postcondition sera : **Le système affiche la liste des requêtes de travail**.
    - Toutes les étapes après l'étape 2 ne sont pas nécessaires, car à l'étape 2 notre objectif est réalisé. Notre but ici est de consulter la liste des requêtes, et non une requête en particulier.

# Diagramme d'activités 
### Respect du formalisme 
- Le formalisme des diagrammes d'activité est bien respecté.
### Contenu du diagramme 
    - Voir le feedback des diagrammes dans le fichier feedback diagramme.

# Analyse 📈 
#### Risques 
- **Risque d'authentification non sécurisée** :
    - Ce n'est pas vraiment un risque, c'est juste que les développeurs ont mal fait leur travail. Les développeurs doivent écrire du code qui s'assure que l'utilisateur a un bon mot de passe.
#### Besoins non fonctionnels 
 - Les besoins non fonctionnels choisis sont justes et bien expliqués.

#### Besoins matériels 
 - La solution pour les besoins matériels est super.
 - Tout est bien mentionné : le déploiement, l'hébergement, etc.
#### Solution de stockage 
    - Idem pour la solution de stockage.

#### Solutions d'intégration 
    - Idem pour la solution d'intégration.

# Prototype 
- Le prototype fonctionne bien, ne génère pas d'erreur.

# Git 
    - Le README dans le document décrit bien le projet.
    - Un release a été fait.
    - Tous les membres de l'équipe ont fait des commits.

# Rapports 
    - Rien à redire pour

 le rapport.
    - Les images s'affichent bien et sont à la bonne dimension.

# Bonus

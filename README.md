An application to display top 120 popular Android repositotries.

Applications pulls and displays following information about Repository:
  - id
  - full_name
  - html_url
  - description
  - created_at date
  - updated_at date
  - stargazers_count
  
Above information is displayed using a Recylerview, list of repositories are sorted by their stargazers_count and ranked accordingly.
User can tap on the item, which will open a webview and take user to the repository page.

- Applications uses Realm database to store the list after fetching it from Github Api.
- Retrofit library is used to fetch the repositories.
- Application has one Activity and 2 Fragments.
- Android architecture components like AndroidViewModel and LiveData is used to display data.
- When user first time starts the application. It fetches top 120 repositories and updates the database. Activity(view) is - observing the database for changes using LiveData and ViewModel. 
- When Database is updated after initial sync , view reacts to the update by updating the recyclerview. 
- Application has a swipe to refresh feature for continuous update.
- User can pull the recyclerview from top to update the recyclerview.

Project contains some Unit tests and Instrumentation tests.
Project is also set-up for proguard ,if we build a release build.

Download debug apk from https://www.bitrise.io/artifact/4889984/p/841267a2e5f45ae128286b1e7294b6ea

 

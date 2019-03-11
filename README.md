# Unacademy Clone

## Installation

Install [Android Studio](https://developer.android.com/studio) based on the OS you are using.



## Execution

After downloading the project in the local machine, Follow the steps below:

```bash
1. Open project folder in the Android Studio: File --> Open --> (Select the project folder)
2. For executing the app: Run --> Run 'app'. 
```

All the files will be compiled and once the build is completed successfully, your app will be installed in the Emulator.



## Structure

`/java/com/unacademyclone` will contain all the java files.

`/activity` folder contains implementation logic of all the pages in the application. 

`/adapter` folder contains logic for rendering different types of list views. i.e If you have Array of objects with size 10, this Adapter files will help you to convert the data object into the UI which represents the particular row. 

`/fragment` folder contains logic for the fragments which are present inside the each activities. 

`/model` folder contains classes with setter and getter functions for each attributes inside the class. 


<h3>Java:</h3>

```
/unacademyclone
  ├─ /activity
  │   ├─ GoalsActivity.java
  │   ├─ MainActivity.java
  │   ├─ PostActivity.java
  │   ├─ SearchAutoCompleteActivity.java
  │   ├─ UserProfileActivity.java
  │
  ├─ /adapter
  │   ├─ AllTopicsItemAdapter.java
  │   ├─ CollectionItemAdapter.java
  │   ├─ CourseAdapter.java
  │   ├─ EducatorAdapter.java
  │   ├─ GoalAdapter.java
  │   ├─ GoalFeedItemAdapter.java
  │   └─ StoryAdapter.java
  │   └─ TopicGroupItemAdapter.java
  │
  ├─ /fragment
  │    ├─ HomeFragment.java
  │    ├─ MyLibraryFragment.java
  │    ├─ PlusFragment.java
  │    ├─ ProfileFragment.java
  │
  ├─ /model
  │   ├─ AllTopicsItem.java
  │   ├─ CollectionItem.java
  │   ├─ CourseItem.java
  │   ├─ Educator.java
  │   ├─ Goal.java
  │   ├─ Story.java
  │   ├─ StoryAuthor.java
  │   ├─ TopicGroupItem.java
```

<h5>XML:</h5>

`/res/layout/` folder contains XML files which specifies the styles and alignment of UI elements. 


```
/res
  ├─ /layout
  │   ├─ activity_goals.xml
  │   ├─ activity_main.xml
  │   ├─ activity_post.xml
  │   ├─ activity_search_autocomplete.xml
  │   ├─ activity_user_profile.xml
  │   ├─ fragment_home.xml
  │   ├─ fragment_my_library.xml
  │   ├─ fragment_plus.xml
  │   ├─ fragment_profile.xml
  │   ├─ row_all_topics.xml
  │   ├─ row_collection_item.xml
  │   ├─ row_course.xml
  │   ├─ row_educator.xml
  │   ├─ row_goal.xml
  │   ├─ row_goal_feed_item.xml
  │   ├─ row_story.xml
  │   ├─ row_topic_group_item.xml  
```


## Glossary

The list below contains some of the basic terminology in Android development and their equivalent in Web Application development.

```bash
Android App   <--->   Web App

.xml File     <--->   .html File
.java File    <--->   .js File
Activity      <--->   HTML page like index.html, profile.html
Fragment      <--->   <frame />
```


## Screenshots

  <img src="https://i.ibb.co/7SBDfNG/Screenshot-2019-03-10-18-25-40-497-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/vPrC9PN/Screenshot-2019-03-10-18-24-53-270-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/nrj2pqy/Screenshot-2019-03-10-18-25-50-876-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/93QT81f/Screenshot-2019-03-10-02-57-39-478-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/QXNWtjT/Screenshot-2019-03-10-02-58-17-008-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/Q998vGg/Screenshot-2019-03-10-02-58-24-240-com-unacademyclone.png" width=340px height=640px/>
  
  <img src="https://i.ibb.co/02416Vs/Screenshot-2019-03-10-02-58-47-662-com-unacademyclone.png" width=340px height=640px/>


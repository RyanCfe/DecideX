# DecideX 

Smart Decision-Making Android App built using Kotlin, Firebase, and SQLite.

## Overview

DecideX helps users make better decisions using a **weighted scoring system** instead of random choice.

It considers:
- Importance
- Desire
- Effort
- Mood
- Urgency
- Goal

---

## Core Idea

The app calculates scores using:

score = (importance × urgency) + (desire × mood) − (effort × penalty) + goal bonus

This makes decisions **context-aware and personalized**

---

## Features

- Firebase Authentication (Login/Register)
- Option rating using SeekBars
- Context input (Goal, Mood, Urgency)
- Smart result with explanation
- SQLite decision history
-  File reader (.txt import)
-  AlertDialogs for confirmations
-  Dynamic UI (Result screen built fully in Kotlin)

---

##  Tech Stack

- Kotlin
- Android Studio
- Firebase Authentication
- SQLite (SQLiteOpenHelper)
- Android UI (ListView, Spinner, SeekBar, GridLayout)

---

##  App Flow

Login → Home → Input → Rating → Questions → Result → Save → History

---

##  Project Structure

- LoginActivity → Firebase login
- RegisterActivity → Account creation
- MainActivity → Home screen
- InputActivity → Add options
- RatingActivity → Rate options
- QuestionActivity → Context + scoring
- ResultActivity → Dynamic UI (no XML)
- HistoryActivity → SQLite history
- FileReaderActivity → Read text files
- DecisionDbHelper → Database helper

---

##  Highlights

- Dynamic UI built entirely in Kotlin (no XML)
- Context-based decision scoring system
- Multi-activity data flow using Intents
- Covers complete Android MAD syllabus

---

## Setup Instructions

This project uses Firebase Authentication.

To run:

1. Create a Firebase project
2. Add Android app: `com.example.decidex`
3. Download `google-services.json`
4. Place it inside `/app` folder

---

---

## Author

Ryan Neshar  

# EUIBuddy Chatbot Project

## Overview:
EUIBuddy is a simple Scala chatbot designed to help users find information about EUI such as faculties, campuses, contacts, GPA calculation, and more.

## Requirements:
- Scala 3.3.1
- sbt 1.11+
- Visual Studio Code

## How to Run:
1. Extract the project ZIP.
2. Open the folder in VS Code.
3. Open the terminal (`Ctrl + ``).
4. Run: 'sbt run'
5. Type your question freely, and EUIBuddy will understand it.
    (e.g: “Show me the faculties” → fac, “Where is the campus?” → where)

## Architecture Notes:
- Language: Scala 3  
- Build Tool: sbt  
- Structure:
- `EUIBuddy.scala` → entry point (runs the chatbot loop)  
- `commands.csv` → maps possible keywords to chatbot commands
- Design: keyword-based command detection with modular functions.

## Usage Instructions
1. Launch the chatbot: Run the project with `sbt run` to start EUIBuddy.

2. Type freely: You can ask questions or enter commands naturally. EUIBuddy will detect what you mean.

3. View available features: Type `menu` to see a full list of features available like `fac`, `where`, `gpa`, `stats`, etc.

4. Exit the chatbot: Type `bye` to quit.


## Available Features:
'menu' - Displays all available commands
'fac' - Lists EUI faculties & programs
'where' - Shows campus locations
'contact' - Provides contact details, including hotline, email, & website
'path' - Displays each faculty’s study plan & tracks availability
'grades' - Shows the portal link to check results
'gpa' - Calculates term GPA/CGPA based on credit hours & grades
'stats' - Tracks total commands & top 3 searched commands
'bye' -  Exits the chatbot & shows a session summary of used commands

## Notes:
- All information is based on EUI’s official resources.
- Always verify information from the official website.

## Students:
- Jana Mohamed Abdelhafeez 23-101097
- Icel Tamer Hassan 23-101104
- Roaa Yasser 24-101384

## Group Name: 
- Lazy Evaluators

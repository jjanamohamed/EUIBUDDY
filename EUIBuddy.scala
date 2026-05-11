import scala.io.{StdIn, Source}
import scala.util.Random
@main def EUIBuddy(): Unit = { // IMPURE - Prints
  println("Welcome to EUIBuddy - Your Smart campus Assistant!")
  println("Type freely, and I'll understand your request.\n")
  startChat(List())} //starts with an empty conversation history

// Chat loop
def startChat(history: List[(String, String)]): Unit = { // Impure - (recursion based on I/O)
  val input = StdIn.readLine("You: ").trim.toLowerCase

  if (input == "bye") { 
    println("EUIBuddy:\nGOODBYE!\n")
    println(showAnalytics(history))
    return}


  val (response, updatedHistory) = handleCommand(input, history) //process command and update history
  println(s"EUIBuddy:\n$response\n")
  startChat(updatedHistory)} //continue chat with the updated history

// CSV Loading - IMPURE
val keywordMap: Map[String, String] = {
  val file = Source.fromResource("commands.csv")
  val lines = file.getLines().drop(1)
  val mapping = lines.map { line =>
    val Array(keyword, command) = line.split(",").map(_.trim)
    keyword -> command
  }.toMap
  file.close()
  mapping}

// Tokenization - PURE
def tokenize(input: String): List[String] = {
  val words = input.toLowerCase.split("\\s+") //splits whitespace
  
  def toList(arr: Array[String], index: Int = 0, acc: List[String] = Nil): List[String] = {
    if (index >= arr.length) acc //base case return accumulated lst
    else toList(arr, index + 1, acc :+ arr(index).trim)} //Recursive adds trimmed word to list
  toList(words)}

// Detecting comma  nd from natural input - PURE
def detectCommand(input: String): Option[String] = {
  val tokens = tokenize(input) //break input into words
  def findMatch(tokens: List[String]): Option[String] = tokens match {
    case Nil => None //base case no more tokens to check
    case head :: tail =>
      def checkKeyword(mapKeys: List[String]): Option[String] = mapKeys match {
        case Nil => None //base case no more keywords to check
        case key :: rest =>
          if (key == head) Some(keywordMap(key)) // Found matching keyword
          else checkKeyword(rest) // Continue searching
      }
      checkKeyword(keywordMap.keys.toList) match {
        case Some(cmd) => Some(cmd) //found command for current token
        case None => findMatch(tail) //recursiom try next token
      }
  }
  findMatch(tokens)}

// Command Handler
def handleCommand(input: String, history: List[(String, String)]): (String, List[(String, String)]) = { //Impure - (prints, reads input, calls impure fns)
  def getValidCommand(userInput: String): String = { // Impure - (inside handleCommand, reads input, prints)
    if (userInput.toLowerCase == "bye") "bye"
    else detectCommand(userInput) match {
      case Some(cmd) if cmd.nonEmpty => cmd
      case _ =>
        println("Sorry, I did not understand that. Can you re-type your question?")
        val retry = StdIn.readLine("You: ").trim //asking user to retry
        getValidCommand(retry) //recursive loop until valid input
    }
  }

  val finalCommand = getValidCommand(input) //ensure we have a valid command

  val response = finalCommand match {
    case "greet" =>
      val responses = List(
        "HELLO! How can I help you today?",
        "HI! Need any assistance?",
        "HEY THERE! How may I support you?")
      responses(Random.nextInt(responses.length))

    case "menu"   => showMenu() 
    case "fac"    => getFaculties()
    case "where"  => getCampuses()
    case "contact"=> getContacts()
    case "grades" => showGrades()
    case "gpa"    => calculateGPA()
    case "stats"  => getStats(history)
    case "path" =>
      val faculty = askFaculty()
      showStudyPlan(faculty) 
  }
  (response, history :+ (input -> response)) //return response and updated history
}

// Helper function for invalid faculty choice in study plan
def askFaculty(): String = { // IMPURE - (reads input, prints messages)
  println("Which faculty's study plan would you like to see? (CIS, ENG, BI, DA)")
  val input = StdIn.readLine().trim.toUpperCase
  input match {
    case "CIS" | "ENG" | "BI" | "DA" => input
    case _ =>
      println("Invalid Faculty: Please re-type one of: CIS, ENG, BI, DA")
      askFaculty() //recursive retry until valid input
  }
}

// Data Mapping
val faculties = Map(
  "CIS" -> "Computing & Information Systems", "ENG" -> "Engineering",
  "BI"  -> "Business Informatics", "DA"  -> "Digital Arts & Design")

val gradePoints: Map[String, Double] = Map(
  "A+" -> 4.0, "A" -> 3.7, "A-" -> 3.4,
  "B+" -> 3.2, "B" -> 3.0, "B-" -> 2.8,
  "C+" -> 2.6, "C" -> 2.4, "C-" -> 2.2,
  "D+" -> 2.0, "D" -> 1.5, "D-" -> 1.0,
  "F"  -> 0.0)

val campuses = Map("Knowledge City" -> "New Administrative Capital", "NAC" -> "New Administrative Capital")

val contacts = Map("Hotline" -> "10071-0001", "Email"   -> "info@eui.edu.eg", "Website" -> "https://eui.edu.eg")

val disclaimer = "\n(Double-check the official EUI pages; details may change.)"

// Menu - PURE
def showMenu(): String =
  """Available Features:
    fac -     Faculties & Programs
    where -   Campus locations
    contact - Contacts & Hotline
    grades -  Where to check results
    gpa -     Calculate your GPA/CGPA
     stats -   Show usage statistics
    path -    Study plan overview
    bye -     Exit chatbot
"""

// Faculties - PURE
def getFaculties(): String =
  faculties.map((abbr, full) => s"$abbr -> $full") 
    .reduce(_ + "\n" + _) + disclaimer 

// Campus Locations - PURE
def getCampuses(): String =
  campuses.map((name, loc) => s"$name -> $loc")
    .reduce(_ + "\n" + _) + disclaimer

// Contacts - PURE
def getContacts(): String =
  contacts.map((a, b) => s"$a: $b")
    .reduce(_ + "\n" + _) + disclaimer

// Study Plan for each Faculty - PURE
def showStudyPlan(faculty: String): String = {
  val explanation = faculty match {
    case "CIS" =>
      """
      Computing & Information Sciences Study Plan:
        
        Duration: 4 Years (8 Terms)
        Structure:
            - Divided into 4 academic years, each with 2 terms.
            - Courses are taken in sequence, with prerequisites for advanced classes.
            - Meeting with an academic advisor helps tailor the plan to your goals.

        Available Tracks:
            1. Computer Science
            2. Artificial Intelligence
            3. Cyber Security
            4. Data Science & Engineering
            5. Software Engineering (coming soon)
            6. Graphics & Game Programming (coming soon)
      """

    case "ENG" =>
      """
      Engineering Study Plan:

        Duration: 5 Years (10 Terms)
        Structure:
            - The program spans 5 academic years, each with 2 terms.
            - Many courses have prerequisites that must be completed first.
            - Meeting with an academic advisor is recommended to tailor your plan to your goals.

        Available Tracks:
            - Communication Systems
            - Electronics
        
        Other Programs:
            - Computer Engineering
            - Industrial Engineering
            - Mechatronics & Robotics
      """

    case "BI" =>
      """
      Business Informatics Study Plan:

        Duration: 4 Years (8 Terms)
        Structure:
            - The program spans 4 academic years, each with 2 terms.
            - Many courses have prerequisites that must be completed first.
            - Meeting with an academic advisor is recommended to align your plan with career goals.

        Available Tracks:
            1. Finance
            2. Business Analytics
            3. Business Technology Management
            4. Digital Marketing & E-Commerce
            5. Accounting (coming soon)
            6. Entrepreneurship & Innovation (coming soon)
      """

    case "DA" =>
      """
      Digital Arts & Design Study Plan:

        Duration: 4 Years (8 Terms)
        Structure:
            - The program spans 4 academic years, each with 2 terms.
            - Some courses require completing earlier ones as prerequisites.
            - Meeting with an academic advisor helps tailor the plan to creative and career goals.

        Available Tracks:
            1. Interaction Design
            2. Animation Arts
            3. Games Design
            4. New Media Arts
            5. Graphic & Media Design
            6. User Experience Design
      """
  }
  explanation + disclaimer
}

// Grades - PURE
def showGrades(): String = "Check your results on the EUI student portal: https://eui.edu.eg/" + disclaimer

// GPA & CGPA calculation
def calculateGPA(): String = { // IMPURE - (reads user input, prints GPA & CGPA)
  def readIntSafe(prompt: String): Int = {
    print(prompt)
    val input = StdIn.readLine().trim
    input.toIntOption match {
      case Some(value) if value > 0 => value
      case _ =>
        println("Invalid input! Please enter a positive integer.")
        readIntSafe(prompt) //retry on invalid input
    }
  }

  def readDoubleSafe(prompt: String): Double = { 
    print(prompt)
    val input = StdIn.readLine().trim
    input.toDoubleOption match {
      case Some(value) if value >= 0 => value
      case _ =>
        println("Invalid input! Please enter a positive number.")
        readDoubleSafe(prompt) //retry on invalid input
    }
  }

  def readGradeSafe(prompt: String): Double = {
    print(prompt)
    val input = StdIn.readLine().trim.toUpperCase
    gradePoints.get(input) match {
      case Some(points) => points //valid grade found in map
      case None =>
        println("Invalid grade! Please enter a valid grade like A, B+, or F.")
        readGradeSafe(prompt) //retry on invalid input
    }
  }

  val numCourses = readIntSafe("How many courses did you study this term? ")

  val (totalPoints, totalCredits) = //calculate weighted grade points and total credits
    (1 to numCourses).foldLeft((0.0, 0.0)) { case ((pointsAcc, creditsAcc), i) =>
      val credit = readDoubleSafe(s"Course $i credit hours: ")
      val grade = readGradeSafe(s"Course $i grade (A+, A, B, ... , F): ")
      (pointsAcc + credit * grade, creditsAcc + credit) //accumulate weighted points and credits
    }

  val gpa = if (totalCredits > 0) totalPoints / totalCredits else 0.0 //term gpa
  println(f"Your term GPA is: $gpa%.2f")

  val prevCgpa = readDoubleSafe("What was your previous CGPA? ")
  val prevCredits = readDoubleSafe("How many total credit hours have you completed? ")

  val newCgpa = //calculate updated CGPA inclusive current term
    if (prevCredits + totalCredits > 0)
    ((prevCgpa * prevCredits) + (gpa * totalCredits)) / (prevCredits + totalCredits) 
    else gpa
  
  f"Your term GPA is: $gpa%.2f\nYour updated CGPA is: $newCgpa%.2f"
}

// Statistics - PURE
def getStats(history: List[(String, String)]): String = {
  val total = history.size 
  val top = history.groupBy(_._1) //group by command type
                   .map((cmd, list) => (cmd, list.size)) //count occurrences
                   .toList
                   .take(3) //top 3

  val topStats =
    if top.isEmpty then "No commands used yet."
    else top.map((cmd, count) => s"$cmd -> $count times")
            .reduce((a, b) => a + "\n" + b)
  s"Total commands: $total\nTop commands:\n$topStats"
}

// Session Summary - PURE
def showAnalytics(history: List[(String, String)]): String = {
  val total = "Total commands used: " + history.size

  val top = history
    .groupBy(_._1) //group by command type
    .map((cmd, list) => (cmd, list.size)) //count each command type
    .toList
    .take(3) //top 3

  val topStr =
    if top.isEmpty then "No commands used."
    else {
      "Most used commands:\n" +
        top.map((cmd, count) => s"$cmd -> $count times")
           .reduce(_ + "\n" + _)
    }
  "Session Summary:\n" + total + "\n" + topStr //final summary
}
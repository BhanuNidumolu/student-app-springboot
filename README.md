# 🎓 Student Management System (Spring Boot + Thymeleaf)

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templates-blue?logo=thymeleaf)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?logo=opensourceinitiative)](LICENSE)

A **CRUD-based web application** built with **Spring Boot, Spring Data JPA (Hibernate), Thymeleaf, and MySQL** to manage student records.  
This project demonstrates how to build a full-stack Java web app with **MVC architecture** and **database integration**.

---

## 🚀 Features
✅ Add new students  
✅ View all students  
✅ Update student details  
✅ Delete students  
✅ Responsive UI built with **Bootstrap**  

---

## 🛠 Tech Stack
- **Backend:** Spring Boot (Spring MVC, Spring Data JPA, Hibernate)  
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 4  
- **Database:** MySQL  
- **Build Tool:** Maven  
- **Language:** Java 17+  

---
<img width="1893" height="993" alt="Screenshot 2025-08-24 154507" src="https://github.com/user-attachments/assets/04f10bcd-81f0-4c86-879c-6e04d76ba6af" />


## 📂 Project Structure
```
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/learn/studentmanagment/
│   │   │   ├── Controller/               
│   │   │   ├── Entity/                    
│   │   │   ├── Repository/                
│   │   │   ├── Service/                   
│   │   │   ├── Service/IMPL/              
│   │   │   └── StudentManagmentApplication.java  
│   │   │
│   │   ├── resources/
│   │   │   ├── templates/                 
│   │   │   │   ├── students.html           
│   │   │   │   ├── create_student.html     
│   │   │   │   └── edit_student.html       
│   │   │   ├── static/                     
│   │   │   └── application.properties      
│   │
│   └── test/java/learn/studentmanagment/   
│
├── .gitignore                              
├── README.md                               
├── LICENSE                                 
├── pom.xml                                 

---
```
## ⚙️ Setup & Installation

### 1️⃣ Clone the repository
```bash
git clone https://github.com/your-username/student-management-system.git
cd student-management-system
```
2️⃣ Configure MySQL
```
Create a database:

sql
Copy
Edit
CREATE DATABASE studentm;
Update src/main/resources/application.properties:
```
properties
```
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/studentm
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```
3️⃣ Run the application
```
bash
Copy
Edit
mvn spring-boot:run
4️⃣ Open in browser
bash
Copy
Edit
http://localhost:8080/students
```
# 📸 Screenshots (UI Previews)
=> Student List Page
<img width="1920" height="1200" alt="Screenshot 2025-08-24 153537" src="https://github.com/user-attachments/assets/b590d20c-b327-4393-bdaf-bb445d4f3633" />
Add Student Form
<img width="1920" height="1200" alt="Screenshot 2025-08-24 153550" src="https://github.com/user-attachments/assets/63a83e5a-1710-41ed-aa3d-108b501e8484" />

Edit Student Form
<img width="1920" height="1200" alt="Screenshot 2025-08-24 153615" src="https://github.com/user-attachments/assets/6f018db5-ba7c-4c52-9361-90879dc5b68b" />

🤝 Contributing
Contributions, issues, and feature requests are welcome!
Feel free to fork this repository and submit a pull request.

📜 License
This project is licensed under the MIT License.

⭐ Don’t forget to star this repo if you found it useful!
---  

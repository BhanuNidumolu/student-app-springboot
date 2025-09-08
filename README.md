# 🎓 Student Management System (Spring Boot + Thymeleaf)

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templates-blue?logo=thymeleaf)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![AWS](https://img.shields.io/badge/AWS-Cloud-orange?logo=amazon-aws)](https://aws.amazon.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?logo=opensourceinitiative)](LICENSE)

A **CRUD-based web application** built with **Spring Boot, Spring Data JPA (Hibernate), Thymeleaf, and MySQL**, deployed on **AWS EC2**, to manage student records.  
This project demonstrates how to build and deploy a full-stack Java web app with **MVC architecture** and **cloud deployment** using AWS.

---

## 🚀 Features
✅ Add new students  
✅ View all students  
✅ Update student details  
✅ Delete students  
✅ Responsive UI built with **Bootstrap**  
✅ Deployed on **AWS EC2** with MySQL integration

---

## 🛠 Tech Stack
- **Backend:** Spring Boot (Spring MVC, Spring Data JPA, Hibernate)  
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 4  
- **Database:** MySQL (hosted locally on EC2 or using AWS RDS)  
- **Cloud:** AWS EC2  
- **Build Tool:** Maven  
- **Language:** Java 17+  

---

## 📂 Project Structure
```
student-management-system/
├── src/
│ ├── main/
│ │ ├── java/learn/studentmanagment/
│ │ │ ├── Controller/
│ │ │ ├── Entity/
│ │ │ ├── Repository/
│ │ │ ├── Service/
│ │ │ ├── Service/IMPL/
│ │ │ └── StudentManagmentApplication.java
│ │
│ ├── resources/
│ │ ├── templates/
│ │ │ ├── students.html
│ │ │ ├── create_student.html
│ │ │ └── edit_student.html
│ │ ├── static/
│ │ └── application.properties
├── .gitignore
├── README.md
├── LICENSE
├── pom.xml
```
yaml
Copy code

---

## ✅ AWS EC2 Deployment Instructions

### 1️⃣ Launch an EC2 instance
- Choose an Amazon Linux 2 or Ubuntu image.
- Open port `8080` (or `80`) in the security group to allow HTTP traffic.
- Create or import an SSH key pair for access.

### 2️⃣ Connect to EC2 via SSH

ssh -i your-key.pem ubuntu@your-ec2-public-ip
3️⃣ Install Java, Maven, and MySQL (if using local DB)
bash
Copy code
sudo apt update
sudo apt install openjdk-17-jdk maven mysql-server -y
4️⃣ Configure MySQL
bash
Copy code
sudo mysql
CREATE DATABASE studentm;
CREATE USER 'root'@'%' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON studentm.* TO 'root'@'%';
FLUSH PRIVILEGES;
EXIT;
Allow remote connections if needed by editing /etc/mysql/mysql.conf.d/mysqld.cnf.

5️⃣ Clone your project and build
bash
Copy code
cd /home/ubuntu/
git clone https://github.com/your-username/student-management-system.git
cd student-management-system
mvn clean package
6️⃣ Configure application.properties
properties
Copy code
```
spring.datasource.url=jdbc:mysql://localhost:3306/studentm
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
Or use AWS RDS and replace the url, username, and password.
```
7️⃣ Run the application
bash
Copy code
```
nohup java -jar target/student-management-system-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```
8️⃣ Access the application
Open your browser and go to:

bash
Copy code
```
http://your-ec2-public-ip:8080/students
```
📸 Screenshots (UI Previews)
Student List Page

<img width="1861" height="1093" alt="Screenshot 2025-09-09 013742" src="https://github.com/user-attachments/assets/c491430e-a14b-42b7-ae21-1d2ad61f36bc" />

Add Student Form
<img width="1920" height="1200" alt="Add Student Form" src="https://github.com/user-attachments/assets/63a83e5a-1710-41ed-aa3d-108b501e8484" />

Edit Student Form
<img width="1920" height="1200" alt="Edit Student Form" src="https://github.com/user-attachments/assets/6f018db5-ba7c-4c52-9361-90879dc5b68b" />

📜 License
This project is licensed under the MIT License.

⭐ Acknowledgements
Built with ❤️ using Spring Boot, Thymeleaf, MySQL, and AWS EC2.

📂 Additional Notes
Ensure that port 8080 (or 80) is open in the EC2 instance's security group.

Consider using a reverse proxy like Nginx for production setups.

For database security, restrict access and use environment variables for credentials.

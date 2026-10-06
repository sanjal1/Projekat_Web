# Browser Games Web App

A web application with simple browser games, inspired by the Friv game site, with additional features like achievements, game categories...

This project was made for the **Web Programming** course at the Faculty of Technical Sciences, Novi Sad. It was a team project for two students.

## About the Project

The assignment was to build a web app that takes inspiration from the Friv game site. We built a simpler version of a browser games portal, where users can pick a game from a list and play it directly in the browser.

On top of the basic game portal, we added: **user accounts, score tracking, leaderboard, game ratings and more features that the admin can access**.

## Features

- Browse a list of available games
- Play games directly in the browser
- Some of the games included: **[Clicker Heroes, Mr Mine, Poker Quest]**
- Added user accounts, score tracking, leaderboard, game ratings
- Admin features: blocking/unblocking users, adding/deleting games and categories, able to see most played game, able to see all of the users scores...

## Technologies

| Part | Technology |
|------|------------|
| Backend | Java, Spring Boot |
| Frontend | Vue.js |
| IDE | IntelliJ IDEA |
| Version control | Git, GitHub |
| Database | H2 |

## Getting Started

### Requirements

- Java **[version 17]** or newer
- Node.js and npm
- Git

### Run the backend

```bash
git clone https://github.com/username/repository-name.git
cd repository-name/backend
./mvnw spring-boot:run
```

The backend starts on `http://localhost:8080`.

### Run the frontend

```bash
cd ../frontend
npm install
npm run serve
```

The frontend starts on `http://localhost:5173` (the port is shown in the terminal).

> The folder names and commands above may differ in this project. Adjust them to match the repository.

## Project Structure

```
src/
├── backend/     # Spring Boot application (Java)
└── frontend/    # Vue.js application
```

## Screenshots
Home page png: <img width="2802" height="1559" alt="image" src="https://github.com/user-attachments/assets/bed2c579-84d1-45d8-b514-b238b4746de0" />

List of games png: <img width="2633" height="1292" alt="image" src="https://github.com/user-attachments/assets/3d792732-340e-4e19-adf6-4aa7632d8e55" />




## Team

- **Sanja Lukač** github: sanjal1
- **Helena Vasić** github: HelenaVasic

## What We Learned

- Building a full-stack web application with a Java backend and a Vue.js frontend
- Connecting a frontend to a Spring Boot backend
- Working as a team and sharing the work with Git

## Course Information

- **Course:** Web Programming
- **Professor:** Miroslav Zarić
- **Faculty:** Faculty of Technical Sciences, Novi Sad

# 🦥 SlothGaming | Android Discovery App

**A modern, reactive Android application for gaming enthusiasts to discover and track their favorite titles.**
Built using industry-standard Jetpack libraries and a robust Micro-Backend architecture to ensure secure and smooth data delivery.

---

## 📱 Features & Highlights

* **Gaming Discovery:** Seamless integration with the **IGDB API** to browse a vast database of games.
* **Reactive UI:** Built with **Nested RecyclerViews** optimized for complex data structures and smooth scrolling performance.
* **Secure Architecture:** Developed a custom **Node.js (Express) Proxy Server** to handle authentication and secure API token management.
* **Modern State Management:** Fully reactive data streams using **Kotlin Flow** and **Coroutines** for asynchronous operations.
* **Offline Support:** Robust local caching implemented with **Room Persistence Library**.

---

## 🛠 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Kotlin |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Dependency Injection** | Dagger Hilt |
| **Concurrency** | Coroutines & Flow |
| **Database** | Room |
| **Network** | Retrofit |
| **Backend (Proxy)** | Node.js + Express |

---

## 🏗 Architecture Overview

The app follows Google's recommended **Clean Architecture** principles:
1.  **UI Layer:** Fragment-based UI using ViewBinding and optimized Layouts.
2.  **Domain Layer:** ViewModel-driven logic ensuring lifecycle-aware data handling.
3.  **Data Layer:** Repository pattern managing the choice between the Remote API and the Local Cache (Room).
4.  **Backend Layer:** A dedicated Node.js server acts as a middleman to protect the IGDB Client ID and Secret.

---

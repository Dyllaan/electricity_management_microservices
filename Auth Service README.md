# Authorisation Microservice

**Port:** 8010  

The purpose of this microservice is to handle the authentication of users to each city’s microservices using Spring Security and an in-memory database ([Spring Security, 2024](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html)).

---

## Database

| Username  | Password  |
|-----------|-----------|
| apgroup   | password  |

- The in-memory database is preloaded with a single user.
- Passwords are encrypted using `BCryptPasswordEncoder` ([Spring.io, 2025](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html)).

---

## Token

- An issued token is valid for **24 hours** for a specific username.
- Tokens, using the **HS256** algorithm, are signed with a secret key.
- The key is loaded from a `.env` file.

---

## Endpoints

### **POST: `/user/login`**
#### **Request** #### 
#### Headers ####
- Content-Type: application/json
#### Body ####
```
{
    "username": "apgroup",
    "password": "password"
}
```
#### **Expected Response** ####
- **Status Code:** `200 OK`
```
{
    "username": "apgroup",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcGdyb3VwIiwiaWF0IjoxNzM2Mzc3MDY5LCJleHAiOjE3MzY0NjM0Njl9.xYEIGK6c9aPND7cZ9E7OUzTHwo3zEoGqJKllhChME6k"
}
```
#### **Other Response Codes** ####
- `401 Unauthorised` If the username or password is an invalid combination.
```
{
    "cause": "Invalid username or password"
}
```
- '500 Internal Server Error' If the server has a problem
```
{
    "cause": "Internal server error"
}
```
---

### **POST: `/user/register`**
#### **Request** #### 
#### Headers ####
`Content-Type: application/json`
#### Body ####
```
{
    "username": "louisfiges",
    "password": "c0ld-in-june@"
}
```
#### **Expected Response** ####
- **Status Code:** `201 Created`
```
{
    "username": "louisfiges",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3Vpc2ZpZ2VzIiwiaWF0IjoxNzM2MzgyODUwLCJleHAiOjE3MzY0NjkyNTB9.rE1E4UOVm8JvTZoYvkUmrfKwPukubX-kE89u2th8ZFQ"
}
```
#### **Other Response Codes** ####
- `400 Bad Request` If the password is too weak.
```
{
    "cause": "Password is too weak"
}
```
- `400 Bad Request` If the user already exists.
```
{
    "cause": "User already exists"
}
```
- `500 Internal Server Error` If the server has a problem
```
{
    "cause": "Internal server error"
}
```
---

### **GET: `/user`**
#### **Request** #### 
#### Headers ####
`Authorization: Bearer TOKEN`  

#### **Expected Response** ####
- **Status Code:** `200 OK`
```
{
    "username": "louisfiges"
}
```
#### **Other Response Codes** ####
- `403 Forbidden` If the token cannot be verified.
---

### **PUT: `/user`**
#### **Request** #### 
#### Headers ####
- `Authorization: Bearer TOKEN`
- `Content-Type: application/json` 

#### Body ####
```
{
    "password": "password1!"
}
```

#### **Expected Response** ####
- **Status Code:** `204 No Content`
#### **Other Response Codes** ####
- `400 Bad Request` If the password is too weak.
```
{
    "cause": "Password is too weak"
}
```
- `500 Internal Server Error` If the server has a problem
```
{
    "cause": "Internal server error"
}
```
---

### **DELETE: `/user`**
#### **Request** #### 
#### Headers ####
- `Authorization: Bearer TOKEN`  

#### **Expected Response** ####
- **Status Code:** `204 No Content`
#### **Other Response Codes** ####
- `500 Internal Server Error` If the server has a problem
```
{
    "cause": "Internal server error"
}
```
---

## References

- Spring Security (2024) *‘In-Memory Authentication’*. Available at: [https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html) (Accessed: 08/01/2025).

- Spring.io (2025) *‘Class BCryptPasswordEncoder’*, BCryptPasswordEncoder (spring-security-docs 6.4.2 API). Available at: [https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html) (Accessed: 8 January 2025).


# Vehicle sales service

Daniel Bondarkov, 2021

***

#### Content of the project

1. [Common information](#common-info)

2. [Technical information](#tech-info)

3. [Ideas or features](#features)

***

<a id="common-info"></a>

##### Common information about the project

* The project is for internship in the ItechArt company.  

* A web service for vehicles sales.  

* Project managment is implemented using Trello service.

> **Justification of such choose:**  
> *Trello* is a convenient mean for small project managment. It gives big amount of tools and these tools are enough for purposes of my project.  
> *Asana* is also great utility for project managment, and it has even more functionality compared with Trello, it's more for big projects with big team of developers. 

* This is gonna be a web service where users can search and buy vehicles and sell their ones.  

> Note: More detailed description of what opportunities are given to users of the service is [below](#features).


***

<a id="tech-info"></a>

#### Technical information  

* The main programming language - Java. 

* Backend framework - Spring boot.  

* Used Workflow model - [Gitflow Workflow][1]

* Other technologies - Git, SQL (MySQL) ...

[1]: <https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow> "Gitflow model"


***

<a id="features"></a>

#### Ideas or features which are being considered for inplementing

Of course, users should have opportunity to search ads which are suitable for their needs. It can be implemented with ad search filter.  


***1. Parameters for ad search filter***

- [ ] type of vehicle (car, motorbike, tractor etc.) {car}
- [ ] brand {audi}
- [ ] model {A8 D4}
- [ ] release year {2015}
- [ ] price range {10 - 15$ thousand}
- [ ] ad's location {Minsk}

> Note: the content in { ... } is just a sample.

***2. Unregistered users can do***

* signing up
    - [x] phone number
    - [x] email
    - [x] password
    - [x] name
- [ ] search and watch any ad out
- [ ] find out common information about the service  


***3. Registered users can do (additionally)***

* Authorization
    - [x] sign in
    - [x] log out

* Editing account parameters
    - [ ] edit name
    - [ ] edit phone number
    - [ ] edit login
    - [ ] edit password
    
* Creating own ads
    - [ ] create an ad
    - [ ] watch list of all ads
    - [ ] maintain an ad

* Ad maintaining
    - [ ] attach photos of a vehicle
    - [ ] edit description of an ad
    - [ ] edit cost of a vehicle
    - [ ] edit title of an ad

* Buy vehicle online 
    - [ ] have personal money account for purchasing
    - [ ] top up a money account using credit cards and/or E-money
    - [ ] purchase item if it is marked with the sign "can be bought online"

* Other features
    - [ ] have message conversations with other users
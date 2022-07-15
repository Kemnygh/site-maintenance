# SMP
A prototype Spark, Java application to keep a track for Site maintenance. Latest Version dated 15-JULY-2022.


#### By Kemuel Nalifuma

## Description
SMP standing for Site Management Platform is a web application build from ground up using Spark and Java and connecting into a postgres Database,
it is meant to keep a track of engineers and sites with the capability to edit, update or delete either. It also gives visibility to details of 
the sites and employees.

## Setup/Installation Requirements
* Postgres Database needs to be installed before running the application.
* Creation of the databases and tables using queries earmarked on the create.sql file.
* Deploy to a server environment to access the application
* Git repository link  https://github.com/Kemnygh/site-maintenance.git

## Features
* Delete function implemented is a soft delete that changes status of a record and does not do a hard delete so records will still be available in the DB.
* Search function searches both engineers and sites at a go and separates the two while giving you the count of found items.


## Known Bugs
* Web application will not work properly with devices that have less than 350px screen size.


## Technologies Used
* IntelliJ
* Java
* HTML
* CSS
* Postgres Database
* SQL
* Spark
* Handlebars
* Bootstrap

## Support and contact details
For any issues, questions, ideas or concerns please contact me through email kemnygh@gmail.com, you can also make suggestions to improve the code.

### License
Licensed under the MIT license.

Copyright (c) 2022 Kemuel Nalifuma

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

% title NOTEBOOK

# NOTEBOOK {.text-center #home}

---

Incidents to follow up:

Ñw* Incident 460538 / 2016 / SSL Error
* Incident 470574 / 2016 / CONSSA migración
* Incident 401485 / 2016 / Add-ons not closing when SAP is Shutdown
* Incident 436412 / 2016 / Could not determine the status for the given GUID
* Incident 470625 / 2016 / No company appears in the CHOOSE COMPANY window

---

Estimado Angel Morales,

Le hemos llamado en varias oportunidades a estos números de teléfno, +50223857941 y +50242725159, y nadie nos ha respondido. 

Puede amablemente decirnos cuando le conviene que le llamemos y poder hablar sobre este incidente.

Hemos verificado en nuestro systema y el cliente "San Isabel Corporation, S.A" (Número de instalación: 20479007) y este cliente tiene las siguientes licencias en el contrato:

* 7 x SAO Business One Limited User
* 7 x users per AddOn

Puede por favor envíar la factura donde se confirma que este cliente tiene mas licencias en su contrato?

Luego, verificaremos la información con el área de contratos.

Muchas gracias.

Anil Kumar
Soporte SAP SME

---

This client wants to secure SSL communications of the license server by using CA root certificates.

It has not been possible to secure the License Server with SSL by using CA root certificates.

To know if it is possible to use CA root certificates to secure SSL communications of the license server. If it is possible, then we would like to know how to configure the server to use the CA root certs.

---

460748

---

C:\"Program Files (x86)"\SAP\"SAP Business One BAS GateKeeper"\sapjvm_8\jre\bin\java -XX:+PrintFlagsFinal -version | findstr /i "HeapSize PermSize ThreadStackSize" 

---

* Slow 


* resource counter
* sql tracers


Processo queue lenght
	* 2 task per core, maximum - resource botleneck

load data 
	* traceAnalyses.sql

blocked 

-- find spi, program, etc
select @@SPID
select * from sys.sysprocesses

-- simulate blocking
begin tran
updata...
rollback


select * from all_traces where transactionID = $variable and EventClass in (10, 12) orber by startTime

---

522225835011
522225733800

---

SAP Business One 9.2 PL06

* **NEW:** Live collaboration
	- Chat.
		+ MSM
		+ Share documents.
			+ It applies same authorizations than in SAP B1. If user is not authorized to see document in B1 he/she will not be able in chat either.
		+ It will be supported by System team.
	- Quick copy


RbCRI0034
A0020001

Informe de comisiones (impagadas/mororos)
select * into tempRDOC from RDOC


---

SELECT TOP 10 T0.[DocNum], T0.[DocType], T0.[DocStatus], T0.[CardName], T0.[DocTotal] FROM OINV T0


---

Hello Dev.

Partner can't apply the workaround. Please let me know how to proceed. 

Partner said that the client can't disable the field 'Bar Code' because the client need to see the description of the bar code. Besides, the mention that it didn't reproduce in SAP 9.1H and that version included the same data and same parameters of the form.

Here the business impact for this issue:

1. Which processes are affected? (Please describe them)
All purchase processes where it is necessary to request the item code (Orders, Goods Receipts, etc ...).

2. This process is part of the core business of your client (Y / N)
YES

3. There is some core business process involved. Please explain why it is part of the client's main processes.
Users who have to enter orders and other purchasing documents are very slow in having to select items according to the barcode format.

4. How many transactions are executed through this business process? (Number per day / week / month)
The client creates 3,000 documents every day, and for each document there are 30 lines. There are 50 users who introduce these documents every day. For this reason, it has a great impact on business management.

5. Of these, how many transactions are affected by this inconvenience. (Number per day / week / month)
The client creates 3,000 documents every day, and for each document there are 30 lines. There are 50 users who introduce these documents every day. For this reason, it has a great impact on business management.

6. What problem has been found (please select one)
A. A transaction cannot be added, executed, or completed.
B. Incorrect / updated translation result is incorrect.
C. A transaction cannot be updated / modified.
D. Printing the transaction is not possible or incorrect.
E. Other (Please explain in detail).

Other: Each time an item should be selected, it takes more than 25 seconds to display the information. The purchasing documents have an average of 30 lines, and are introduced about 200 documents per day, but there are a lot of documents with more than 1000 lines.

---

460583
SAP certificate

---

RbCRI0034
A0020001

Informe de comisiones (impagadas/mororos)
select * into tempRDOC from RDOC
where Author <> 'S

---

Allan cheesman
1298216 - Rugged deptop inccreate
Navigator business Solutions
311806187
8811
They made an update change on authorizations then they saved changes.

Expected result: Change made to authorizations all saved in proper way.

466016


---

454254

---

4611991

---

<div class="tabla">

|header1|header2|
|--------|-------|
|row1col1|row1col2|
|row2col1|row2col2|

</div>

---

449234

Related incidents:  
Incident 325479 / 2016 / Error uploading DB via RSP

---

Info Doc 283226 / 2016 / Upgrade Failed / ( Restricted )
Info Doc 903665 / 2015 / Upgrade error / ( Restricted )  

+ Upgrade SQL 2008 R2 to latest patch (SP3 - 10.50.6000.34)
+ Enable enough memory in SQL.
	- Go to SQL server management studio> select the yourinstance name> right click to 'properties' > select 'Memory"
	- Here look out for the configuration "SQL server max memory"
	- assign this value as 60%-70% of the total physical RAM available. For example , if on the database server, the RAM installedis 10 GBs then SQL max memory should be 6GB-7GBs.
	- click Ok
	- restart SQL instance , the SQL will be limited to use the memorybuffer pool as per the value configured for the SQL server max memory. 


---

Dear Brian,

Unfortunately development will not be help us with a webex. This problem does not seem to be related to business one, probably it is related to the environment. If it was Business One it would have reproduced in your development server. So, developers will not be able to deliver a fix patch unless we get to reproduce the problem in a testing environment.

If you think that the problem can be reproduced in a different environment I would appreciate you to send us a copy of one or two of the affected databases. Then, we will create the environment so that developers can find the bug. Please, let me know if you want to try to enable a FTP container.

If definitively it does not reproduce in any other environment, we will need to do a deeper troubleshooting.

Because we are 100% sure that any of known issues affects the client's environment we will jump on the collection of logs and MSSQL profiler files. The known issues are covered in notes 1708426, 1961206, 2372759.

Could you please collect the logs of SAP Business One and the SQL trace at login, see the steps below. (If you'd rather we can set a webex to collect the data).

*  Run an SQL Trace at login in order to capture the events (attached is a document how to setup the trace and run it on the PID of SAP Business One client) 

* Enable logs from SAP Business One. Go to Help - Support desk - logger settings. Then enable Errors, warnings, info in Business Information Level. Besides you can define the folder where the logs will be saved in.

Thank you 

David Cuervo
SAP SME Support Consultant


---

[[ ]{.glyphicon .glyphicon-home key="val"} Home](#home)

---

The indicators are not the best. Since I was assigned to the second allocation (Performance) the PMT and PCC went down, since then it has been impossible to keep good numbers.

Before it was easier to get good PCC, but lastly is more and more difficult to do so. The expectations of the partner has changed, more and more they avoid the L1/L2 troubleshooting which means that they expect more from SAP SME support in terms of basic task. To give a punctual example, when a re-installation of the system is required during a webex, partner and client expect B1 SAP Support consultant to do that, the knowledge to so is there, but the time is not. I could do the re-installation but that task is time consuming and the consequence is to get bad MTP from other incidents.

In   summary, to get high PCC and good MTP is kind of impossible. To bring a good service focused on customer service requires time, but it affects the MTP directly. In the other side to have work in terms to get a good MPT means that the quality of the answer is low, then a high MPT affects the customer satisfaction.

Key Areas:

+ Communication with partners. It is good, I like to do webex and do the stuff on real environments. For me, every problem that I can solve during a webex is an opportunity to learn.
+ Knowledge lever. More and more I get more used to Hana and Performance. I still have a lack of knowledge on incidents related to performance on Hana.
+ Pro-activenes. I feel i am very proactive. I like to test as much as possible all the solutions that I am about to deliver, but i takes time and it always affects my MPT indicator.
+ Understanding business impact. I think i ha


----

437569  
John Clavijo.  
Cynthia Limaira  

Ese mismo dia se hizo el backup system, y el documento orientado a instalacion de hana

Cliente no puede generar reportes personalizados, porque se eliminan los store procedures.

------------------------------ 

Pablo's scenario  
Tec ticket to updrade to hana  
233030  
Incident 409652  

------------------------------



433450  
+523330035477 -> Elvis Florez

-----





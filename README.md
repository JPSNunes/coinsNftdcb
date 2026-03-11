Gonçalo Vieira fc60446
João Nunes fc59806

To run the project you need the BFT-SMaRT library as seen in the TP class. Put the project files inside src/main/java/dcb/dti. Then, open a terminal in the root folder of the project to build it with ./gradlew installDist.
Before running, go to library/build/install/library/config and open the server.config file. Modify the last line to "system.optimizations.readonly_requests = true". This ensures some operations such as MY_COINS work.

We developed and tested the application on Windows using the Ubuntu terminal. To run, go to build/install/library and open 6 terminals (4 for the servers and 2 for the clients). In the first 4 run ./smartrun.sh dcb.dti.DTIServer 0, 1, 2 and 3. Wait until you see "Ready to process operations". After that run on one of the remaining terminals ./smartrun.sh dcb.dti.DTIClient 4 and lastly ./smartrun.sh dcb.dti.DTIClient 1 on the last terminal. Client 4 is the only one that can run MINT to create coins.

The application was tested with the commands listed above. Every operation worked as expected, when used as intended. As there was no design requirement for defensive programming, commands were not implemented defensively. Purposely using them in an incorrect manner (e.g. giving a name as an argument when a number is expected) may result in a client crash.
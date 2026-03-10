Gonçalo Vieira fc60446
João Nunes fc59806

To run the project you need the BFT-SMaRT library as seen in the TP class. Put the project files inside src/main/java/dcb/dti/ and build with ./gradlew installDist.
Before running, go to library/build/install/library/config and change the last line to system.optimizations.readonly_requests to true. Otherwise some operations such as MY_COINS won't work.

We developed and tested this on Windows using the Ubuntu terminal. To run, go to build/install/library and open 6 terminals (4 for the servers and 2 for the clients). In the first 4 run ./smartrun.sh dcb.dti.DTIServer 0, 1, 2 and 3. Wait until they say "Ready to process operations". After that run on the other 2 terminals ./smartrun.sh dcb.dti.DTIClient 4 and ./smartrun.sh dcb.dti.DTIClient 1. Client 4 is the only one that can run MINT to create coins.

We tested with this amount of servers/clients and every operation worked fine.
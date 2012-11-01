bp_mips
=======

This is a course project for computer architect
The main goal in this project are
1. implment tomasulo algorithms for instruction piplineing
2. implement correlation branch predictor to remove stalls in fetch 
3. run bench marking
  3.1 GCD
  3.2 bula

4. simulate with multi thread and cache?



In this implementatoin
1. there are issue, read , execute , writeback stage for each instruction
2. issue can only be made if no one occupy current register
3. read only made when two registers are free or already wrote back
4. execute followed read, forwarding?
5. writeback write back value and release register


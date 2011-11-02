      10  ROOT.
        15  A PIC X(6).
        15  B PIC XXXX.
        15  C PIC S9(5).
        15  D PIC 9(4).
        15  SUB OCCURS 2.
          20 E PIC A(3).
          20 F PIC AAA.
        15  G PIC 9(5)V99
          USAGE IS BINARY.
        15  H PIC 9(4)V99
          USAGE IS PACKED-DECIMAL.
        15  I
          USAGE IS COMP-1.
        15  J
          USAGE IS COMP-2.
        15  K PIC 9(5)V9(2)
          USAGE IS COMP-5.
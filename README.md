# bank-kata-with-clojure

[Bank kata](https://github.com/sandromancuso/Bank-kata) in haskell.

## The problem to solve
```
Given a client makes a deposit of 1000 on 10-01-2012
And a deposit of 2000 on 13-01-2012
And a withdrawal of 500 on 14-01-2012
When she prints her bank statement
Then she would see
date || credit || debit || balance
14/01/2012 || || 500.00 || 2500.00
13/01/2012 || 2000.00 || || 3000.00
10/01/2012 || 1000.00 || || 1000.00
```
## Run it

```shell
lein test
```

## The solution

[Here](/test/bank_kata_with_clojure/acceptance_test.clj), the acceptance test for the exercise.

[Here](/test/bank_kata_with_clojure/account_test.clj), the unit tests.

[Here](/src/bank_kata_with_clojure/account.clj), the solution.

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

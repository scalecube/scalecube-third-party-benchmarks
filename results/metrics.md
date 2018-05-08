**Conditions**
_Pre-populate_: 5M records
_Reads_: Read random item from 5M
_Writes_: Write 5M records from each thread (4 threads = num of cores)
_Hardware_: Intel® Core™ i5-7200U CPU @ 2.50GHz × 4, 64-bit, Ubuntu 16.04 LTS
**Table of results**
---

| DB| Reads(ns) | Reads(r/s) | Writes (ns) | Writes (w/s) |
| --- | --- | --- | --- | --- | 
| Chronicle | 7363 | 258162 | 9973 | 146192 |
| RocksDB | 50448 | 82803 | 25871 | 71318 |
| LMDB | 6394 | 167742 | 28513 | 76439 |
| MVStore | 110358 | 28896 |  |  |
**Conditions**
_Pre-populate_: 5M records
_Reads_: Read random item from 5M
_Writes_: Write 5M records from each thread (4 threads = num of cores)
_Hardware_: Intel® Core™ i5-7200U CPU @ 2.50GHz × 4, 64-bit, Ubuntu 16.04 LTS
**Table of results**
---

| DB| Reads(ns) |Writes (ns) |
| --- | --- | --- |
| Chronicle | 17770 | **10277** |
| RocksDB | 37 | 9596 | 
| LMDB | **34** | 34936 |
| MVStore | 254521 | no |
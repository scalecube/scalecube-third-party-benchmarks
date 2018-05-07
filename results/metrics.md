**Conditions**
_Pre-populate_: 5M records
_Reads_: Read random item from 5M
_Writes_: Write 5M records from each thread (4 threads = num of cores)
_Hardware_: Intel® Core™ i5-7200U CPU @ 2.50GHz × 4, 64-bit, Ubuntu 16.04 LTS
**Table of results**
---

| DB|Reads (r/sec) | Reads(ns) | Writes (w/sec) | Writes (ns) |
| --- | --- | --- | --- | --- |
| Chronicle | 170142 | 17770 | **149385** | **10277** |
| RocksDB | 403310 | 37 | 98330 | 9596 | 
| LMDB | **382013** | **34** | 64846 | 34936 |
| MVStore | 13774 | 254521 | no | no |
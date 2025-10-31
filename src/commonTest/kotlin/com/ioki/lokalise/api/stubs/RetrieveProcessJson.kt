package com.ioki.lokalise.api.stubs

val retrieveProcessJson = """
{
  "project_id": "767946315e9866d013c1d0.08299127",
  "process": {
    "process_id": "2e0559e60e856555fbc15bdf78ab2b0ca3406e8f",
    "type": "file-import",
    "status": "finished",
    "message": "",
    "created_by": 1234,
    "created_by_email": "example@example.com",
    "created_at": "2020-04-20 13:43:43 (Etc/UTC)",
    "created_at_timestamp": 1587390223,
    "download_url": "",
    "details": {
      "files": [
        {
          "status": "finished",
          "message": "",
          "name_original": "index.json",
          "name_custom": "index.json",
          "word_count_total": 2,
          "key_count_total": 1,
          "key_count_inserted": 0,
          "key_count_updated": 0,
          "key_count_skipped": 1
        }
      ]
    }
  }
}
""".trimIndent()
package com.ioki.lokalise.api.stubs

val retrieveProjectJson = """
    {
      "project_id": "string",
      "project_type": "string",
      "name": "string",
      "description": "string",
      "created_at": "string",
      "created_at_timestamp": 0,
      "created_by": 0,
      "created_by_email": "string",
      "team_id": 0,
      "base_language_id": 0,
      "base_language_iso": "string",
      "settings": {
        "per_platform_key_names": true,
        "reviewing": true,
        "auto_toggle_unverified": true,
        "offline_translation": true,
        "key_editing": true,
        "inline_machine_translations": true,
        "branching": true,
        "segmentation": true,
        "custom_translation_statuses": true,
        "custom_translation_statuses_allow_multiple": true,
        "contributor_preview_download_enabled": true
      },
      "statistics": {
        "progress_total": 0,
        "keys_total": 0,
        "team": 0,
        "base_words": 0,
        "qa_issues_total": 0,
        "qa_issues": {
          "not_reviewed": 0,
          "unverified": 0,
          "spelling_grammar": 0,
          "inconsistent_placeholders": 0,
          "inconsistent_html": 0,
          "different_number_of_urls": 0,
          "different_urls": 0,
          "leading_whitespace": 0,
          "trailing_whitespace": 0,
          "different_number_of_email_address": 0,
          "different_email_address": 0,
          "different_brackets": 0,
          "different_numbers": 0,
          "double_space": 0,
          "special_placeholder": 0,
          "unbalanced_brackets": 0
        },
        "languages": [
          {
            "language_id": 0,
            "language_iso": "string",
            "progress": 0,
            "words_to_do": 0
          }
        ]
      }
    }
""".trimIndent()

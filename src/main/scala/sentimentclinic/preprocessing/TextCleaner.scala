package sentimentclinic.preprocessing


object TextCleaner {
  def clean(text: String): String = {
    text.toLowerCase
      .replaceAll("[^a-zA-Z0-9\\s]", "")  // Remove punctuation
      .replaceAll("\\s+", " ")            // Normalize spaces
      .trim
  }
}




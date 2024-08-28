# InstagramFeedClone-Compose

Hey! Are you searching for how to play videos like Instagram, TikTok, or LinkedIn in Android?

Look no further! This project provides a solution for:

https://github.com/user-attachments/assets/bdcea9f1-b9bd-4875-a533-a73b71c91d28

**Instagram-Feed-Compose** is a Jetpack Compose-based Android project designed to replicate the Instagram feed experience. If you're looking for an Instagram feed clone that incorporates modern UI practices and efficient media handling, this project is a perfect fit.

## Features

- **Jetpack Compose**: Utilizes Jetpack Compose for a modern, declarative UI approach.
- **ExoPlayer Integration**: Employs ExoPlayer for seamless video playback. Videos play based on their visibility percentage on the screen; videos with more than 30% visibility are played, with the most visible video prioritized.
- **Kotlin**: Developed entirely in Kotlin for a streamlined and robust experience.
- **Customizable Feed**: 
  - **Feed Data**: Modify the local `feed_data.json` file to include your own video and image links. This enables you to customize the content of the feed to suit your needs.
- **URL-Based Video/Image**: 
  - **Non-Local Media**: Videos and images themselves are not stored locally; only their URLs are provided in the JSON file. This setup ensures that media is fetched and displayed from external sources, rather than being embedded within the app.
- **AutoPlay Videos

## Setup

1. **Clone the Repository**

    ```bash
    git clone https://github.com/yourusername/Instagram-Feed-Compose.git
    ```

2. **Open the Project**

    Open the project in Android Studio.

3. **Modify Feed Data**

    Edit the `feed_data.json` file located in `src/main/assets/` to include your own video and image URLs. Ensure the JSON includes the `aspect_ratio` for each media item to maintain consistent post sizes. This prevents posts from fluctuating in size after loading videos or images.

4. **Build and Run**

    Build and run the project on an Android device or emulator.

## Usage

- **Home Feed**: Swipe through the feed to view various media items.
- **Video Playback**: Videos play based on their visible percentage on the screen. The project is configured to play videos with more than 30% visibility. This threshold is controlled by the `minimumVisibilityPercentageRequiredToPlayVideo` variable, and the video that is the most visible will be prioritized for playback.
- **Interactive Elements**: Use the like, comment, and share buttons to interact with the content.

## Contributing

Feel free to submit pull requests or open issues if you have suggestions or improvements. Contributions are welcome!

## Upcoming Improvements

Stay tuned for more enhancements and features that are on the way!

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


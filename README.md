# Restalo

Restalo is an API for managing restaurant reservations. It provides functionalities for creating and managing
reservations, searching for restaurants and reservations, and reviewing restaurants.

[![npm version](https://img.shields.io/npm/v/my-package.svg)](https://www.npmjs.com/package/my-package)
[![Known Vulnerabilities](https://snyk.io/test/github/your-username/your-repo/badge.svg)](https://snyk.io/test/github/your-username/your-repo)
[![GitHub Actions](https://github.com/your-username/your-repo/workflows/CI/badge.svg)](https://github.com/your-username/your-repo/actions)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites

- Java 21
- Maven 3.x

### Installation

Clone the repository:

```bash
git clone https://github.com/username/restalo.git
cd restalo
```

Build the project:

```bash
mvn clean install
```

Run the project:

```bash
mvn spring-boot:run
```

Docker build image

```bash
docker build -t restalo .
```

Docker run image

```bash
docker compose up -d
```

Docker stop image

```bash
docker compose down -v
```

## Open Source files

For more information about the project and how to contribute, please refer to the following files:

- [CONTRIBUTING](CONTRIBUTING.md)
- [CODE_OF_CONDUCT](CODE_OF_CONDUCT.md)
- [LICENSE](LICENSE)

## License

This project is lisenced under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

Thanks to all contributors who participated in this project and helped make it better.

# Genetic Algorithm with Temporal Logic for Automated Marking

**GATLAM** (Genetic Algorithm with Temporal Logic for Automated Marking) is an experimental automated assessment strategy designed to improve how student programming assignments are evaluated at scale. Unlike traditional automated tools that rely on static test cases, GATLAM uses a **Genetic Algorithm (GA)** to dynamically evolve test input data in order to uncover subtle or complex bugs in student submissions.

## Key Features

- **Genetic Algorithm-based Test Generation**  
  Automatically evolves test inputs to explore edge cases in student code.

- **Temporal Logic Fitness Function**  
  Uses **Linear Temporal Logic (LTL)** to define and detect violations of expected program behaviour.

- **Memory-guided Input Biasing**  
  Remembers failure-inducing input patterns to guide future generations toward fault discovery.

- **Constructive, Personalised Feedback**  
  Provides students with concrete failing test cases, supporting both formative and summative assessment.

## Motivation

Traditional automated assessment tools (e.g., FitchFork, BOSS) rely on instructor-crafted, static test cases. These can miss specific program faults and provide vague feedback. GATLAM addresses these issues by automating both test input generation and feedback creation, aiming for more **thorough, fair, and informative assessments**.

## Scope

> ⚠️ GATLAM focuses on test input generation and automated correctness evaluation. Features such as plagiarism detection, grading, or student reporting interfaces are **not** included in this repository.

## Where to access the different components:

- **GA**
The GA component of GATLAM can be accessed in the GA branch.

- **Interpreter**
An example of an interpreter can be accessed in the Interpreter branch.

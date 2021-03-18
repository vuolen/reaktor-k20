![Heroku](https://heroku-badge.herokuapp.com/?app=reaktor-k20)

[DEMO HERE](https://reaktor-k20.herokuapp.com/)

The website might load slowly at first, if you are the first user in a while to access it.
Heroku automatically kills the process after a while of inactivity, so it has to start up again. After start up it should work normally though.

# reaktor-k20

My solution for Reaktor's 2020 summer job pre-assignment:

>On Debian and Ubuntu systems, there is a file /var/lib/dpkg/status, that holds information about software packages that the system knows about. Write a small program in a programming language of your choice that exposes some key information about packages in the file via an HTML interface.

## Usage

1. This project requires [Leiningen](https://leiningen.org/).
2. Download/clone the project source from GitHub.
3. Navigate to the project directory with your terminal and run `lein ring server`
4. The website should automatically open. If not, then navigate to `localhost:8000` with your browser of choice

You can run the tests with `lein test` although the test suites' coverage is quite small, they were mostly to aid the design process.

## Documentation

### Parser (`parser.clj`)

The parser produces output like this from a control file:
```
{
  "package_name" {
    :Package "package_name"
    :Description ["Synopsis line" [" Paragraphs are stored" " in vectors and split by lines"] 
                  [" Lines starting with" "  two or more spaces" "  are displayed verbatim without word breaks"]]
    :Depends ["dependency1" "dependency2"]
    :Reverse-Depends ["dependencyA" "dependencyB"]
  }
}
```

### HTML generator (`htmlgen.clj`)

The HTML generator produces output like this from a parsed package (not 100% accurate, but conveys the structure adequately)
```
<div class="package">
  <h1 class="name"> package_name </h1>
  <div class="description">
    Synopsis line
    <p class="paragraph">
      Paragraphs are stored in vectors and split by lines
    </p>
    <p class="paragraph>
      Lines starting with
      <pre class="verbatim">
        two or more spaces
      </pre>
      <pre class="verbatim">
        are displayed verbatim without word breaks
      </pre>
    </p>
  </div>
  <div class="dependency-list">
    <a href="dependency1">dependency1</a>
    <a href="dependency1">dependency2</a>
  </div>
  <div class="dependency-list">
    <a href="dependencyA">dependencyA</a>
    <a href="dependencyB">dependencyB</a>
  </div>
</div>
```

### The core (`core.clj`)

Contains the main handler and serves the index page and the package pages.

## Contributing

Although this is just a pre-assignment project, feel free to contribute. Open issues or pull requests and I'll take a look at them. Only thing to keep in mind is that I would prefer this project to stay simple, so extravagant features might not be suitable.

## License

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

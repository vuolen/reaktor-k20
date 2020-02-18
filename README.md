![Heroku](https://heroku-badge.herokuapp.com/?app=reaktor-k20)

# reaktor-k20

My solution for Reaktor's 2020 summer job [pre-assignment](https://www.reaktor.com/junior-dev-assignment/).

## Usage

1. This project requires [Leiningen](https://leiningen.org/).
2. Download/clone the project source from GitHub.
3. Navigate to the project directory with your terminal and run `lein ring server`
4. The website should automatically open. If not, then navigate to `localhost:8000` with your browser of choice

## Documentation

### Parser

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

### HTML generator

The HTML generator produces output like this from a parsed package
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

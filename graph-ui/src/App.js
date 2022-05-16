import './App.css';
import Main from "./components/Main";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import ApolloClientProvider from "./ApolloClientProvider";

function App() {
    return (
        <div className="App">
            <ApolloClientProvider>
                <ThemeProvider theme={createTheme({palette: {mode: 'dark'}})}>
                    <CssBaseline/>
                    <Main/>
                </ThemeProvider>
            </ApolloClientProvider>
        </div>
    );
}

export default App;

import "./App.css";
import "./css/style.css";
import React from "react";
import Login from "./pages/Login";
import AppShell from "./AppShell";
import {
  BrowserRouter,
  Navigate,
  Outlet,
  Route,
  Routes,
  redirect
} from "react-router-dom";
import HomeMedico from "./pages/HomeMedico";
import Pazienti from "./pages/Pazienti";
import Schedules from "./pages/Schedules";
import Profilo from "./pages/Profilo";
import About from "./pages/About";
import Dispositivi from "./pages/Dispositivi";
import Menu from "./components/Menu";
import MenuPaziente from "./components/MenuPaziente";
import Fascicolo from "./pages/Fascicolo"
import Registrazione from "./pages/Registrazione"
import HomePaziente from "./pages/HomePaziente"
import Norme from "./pages/Norme";
import HomeAdmin from "./pages/HomeAdmin";
import ProfiloMedico from "./pages/ProfiloMedico";
import jwt from "jwt-decode"
import Error404 from "./pages/Error404";
import HomeCaregiver from "./pages/HomeCaregiver";
import MenuCaregiver from "./components/MenuCaregiver";

function App() {
  const AuthenticatedRoutePaziente = () => {
    if(!localStorage.getItem("token")) {
      <Navigate to={"/Login"} />
    }
    else {
      if(jwt(localStorage.getItem("token")).ruolo == "PAZIENTE") {
        console.log(jwt(localStorage.getItem("token")).ruolo)
        return (
          <AppShell>
        <MenuPaziente /> <Outlet />
      </AppShell>
        )
      }
    }
  }


  const AuthenticatedRouteMedico = () => {
    if (!localStorage.getItem("token")) {
      <Navigate to={"/Login"} />
    } 
    else {
      if(jwt(localStorage.getItem("token")).ruolo == "MEDICO")
      return(
      <AppShell>
        {" "}
        <Menu /> <Outlet />{" "}
      </AppShell>
    );
  };
}

const AuthenticatedRouteAdmin= () => {
    if (!localStorage.getItem("token")) {
        <Navigate to={"/Login"} />
    }
    else {
        if(jwt(localStorage.getItem("token")).ruolo == "ADMIN")
            return(
                <AppShell>
                    {" "}
                    <Outlet />{" "}
                </AppShell>
            );
    };
}

    const AuthenticatedRouteCaregiver= () => {
        if (!localStorage.getItem("token")) {
            <Navigate to={"/Login"} />
        }
        else {
            if(jwt(localStorage.getItem("token")).ruolo == "CAREGIVER")
                return(
                    <AppShell>
                        {" "}
                        <MenuCaregiver /> <Outlet />{" "}
                    </AppShell>
                );
        };
    }


  const AppRoutes = () => {
    return (
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="Registrazione" element={<Registrazione />} />
          <Route path="/" element={<AuthenticatedRouteAdmin />}>
              <Route exact path="/" element={<Navigate to="/Login" replace />} />
              <Route
                  path="HomeAdmin"
                  element={
                      <AppShell>
                          <HomeAdmin/>
                      </AppShell>
                  }
              />
          </Route>
        <Route path="/" element={<AuthenticatedRoutePaziente />}>
        <Route path="/" element={<Navigate to="/Login" replace />} />
        <Route
            path="Dispositivi"
            element={
              <AppShell>
                <Dispositivi />
              </AppShell>
            }
          />
          <Route
              path="Fascicolo"
              element={
                <AppShell>
                  <Fascicolo />
                </AppShell>
              }
          />
          <Route
              path="HomePaziente"
              element={
                <AppShell>
                  <HomePaziente />
                </AppShell>
              }/>
              <Route
            path="Profilo"
            element={
              <AppShell>
                <Profilo />
              </AppShell>
            }
          />
          <Route
            path="Norme"
            element={
              <AppShell>
                <Norme />
              </AppShell>
            }
          />
        </Route>
        <Route path="/" element={<AuthenticatedRouteMedico />}>
          <Route exact path="/" element={<Navigate to="/Login" replace />} />
      
          <Route
            path="HomeMedico"
            element={
              <AppShell>
                <HomeMedico />
              </AppShell>
            }
          />
         
          <Route
            path="Schedules"
            element={
              <AppShell>
                <Schedules />
              </AppShell>
            }
          />
          <Route
            path="Pazienti"
            element={
              <AppShell>
                <Pazienti />
              </AppShell>
            }
          />
          <Route
            path="ProfiloMedico"
            element={
              <AppShell>
                <ProfiloMedico />
              </AppShell>
            }
          />
          <Route
            path="About"
            element={
              <AppShell>
                <About />
              </AppShell>
            }
          />
        </Route>
          <Route path="/" element={<AuthenticatedRouteCaregiver />}>
              <Route exact path="/" element={<Navigate to="/Login" replace />} />

              <Route
                  path="HomeCaregiver"
                  element={
                      <AppShell>
                          <HomeCaregiver />
                      </AppShell>
                  }
              />

              <Route
                  path="Schedules"
                  element={
                      <AppShell>
                          <Schedules />
                      </AppShell>
                  }
              />
              <Route
                  path="Pazienti"
                  element={
                      <AppShell>
                          <Pazienti />
                      </AppShell>
                  }
              />
              <Route
                  path="ProfiloMedico"
                  element={
                      <AppShell>
                          <ProfiloMedico />
                      </AppShell>
                  }
              />
              <Route
                  path="About"
                  element={
                      <AppShell>
                          <About />
                      </AppShell>
                  }
              />
          </Route>
        <Route path="*" element={<Error404/>} />
      </Routes>
    );
  };

  return (
    <div className="sfondoPagina contenitorePaginaHomePage">
      <BrowserRouter>
        <AppRoutes />
      </BrowserRouter>
    </div>
  );
}

export default App;
